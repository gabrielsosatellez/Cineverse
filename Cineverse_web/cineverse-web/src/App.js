import { useEffect, useState } from "react";
import LoginForm from "./components/auth/LoginForm";
import SplashScreen from "./components/auth/SplashScreen";
import Header from "./components/common/Header";
import Sidebar from "./components/common/Sidebar";
import MoviesSection from "./components/movies/MoviesSection";
import RoomsSection from "./components/rooms/RoomsSection";
import SessionsSection from "./components/sessions/SessionsSection";
import TicketsSection from "./components/tickets/TicketsSection";
import StarWarsSection from "./components/common/StarWarsSection";
import UsersSection from "./components/common/UsersSection";
import ChatWidget from "./components/common/ChatWidget";
import { fetchData } from "./utils/api";

function App() {
  // Estado de autenticación del usuario
  const [currentUser, setCurrentUser] = useState(() => {
    try {
      const u = localStorage.getItem("currentUser");
      return u ? JSON.parse(u) : null;
    } catch (e) {
      return null;
    }
  });

  const [isRegister, setIsRegister] = useState(false);
  const [showSplash, setShowSplash] = useState(true);

  // Estado de la interfaz de usuario
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [section, setSection] = useState("MOVIES");

  // Estado de los datos principales de la aplicación
  const [movies, setMovies] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [selectedSession, setSelectedSession] = useState(null);
  const [takenSeats, setTakenSeats] = useState([]);
  const [showSeatModal, setShowSeatModal] = useState(false);

  // Carga inicial de datos tras la autenticación del usuario
  useEffect(() => {
    if (currentUser) {
      fetchData("http://localhost:8080/api/movies").then(setMovies);
      fetchData("http://localhost:8080/api/rooms").then(setRooms);
      fetchData("http://localhost:8080/api/sessions").then(setSessions);
    }
  }, [currentUser]);

  // Renderiza la pantalla de splash inicial
  if (showSplash) {
    return (
      <SplashScreen onComplete={() => setShowSplash(false)} />
    );
  }

  // Renderiza el formulario de autenticación si no hay usuario logueado
  if (!currentUser) {
    return (
      <LoginForm
        onLoginSuccess={(user) => {
          setCurrentUser(user);
          try {
            localStorage.setItem("currentUser", JSON.stringify(user));
          } catch (e) {}
        }}
        isRegister={isRegister}
        setIsRegister={setIsRegister}
      />
    );
  }

  // Renderiza la aplicación principal una vez autenticado el usuario
  return (
    <div className="min-h-screen bg-gray-100">
      <Header
        currentUser={currentUser}
        onMenuToggle={() => setSidebarOpen(!sidebarOpen)}
      />

      <Sidebar
        isOpen={sidebarOpen}
        onSectionChange={(section) => {
          setSection(section);
          setSidebarOpen(false);
        }}
        onLogout={() => {
          // Limpia la información de sesión y datos persistidos del usuario
          if (currentUser?.email) {
            try {
              localStorage.removeItem(`cineverse_chat_messages_${currentUser.email}`);
            } catch (e) {}
          }
          setCurrentUser(null);
          try {
            localStorage.removeItem("currentUser");
          } catch (e) {}
          setSidebarOpen(false);
        }}
        userRole={currentUser.role}
      />

      <main className="p-6 pt-24">
        {section === "MOVIES" && (
          <MoviesSection
            movies={movies}
            setMovies={setMovies}
            isAdmin={currentUser.role !== "USER"}
            sessions={sessions}
            setSessions={setSessions}
            selectedSession={selectedSession}
            setSelectedSession={setSelectedSession}
            setTakenSeats={setTakenSeats}
          />
        )}

        {section === "ROOMS" && (
          <RoomsSection
            rooms={rooms}
            setRooms={setRooms}
            sessions={sessions}
            setSessions={setSessions}
            isAdmin={currentUser.role !== "USER"}
            selectedSession={selectedSession}
            setSelectedSession={setSelectedSession}
            setTakenSeats={setTakenSeats}
            setShowSeatModal={setShowSeatModal}
          />
        )}

        {section === "SESSIONS" && (
          <SessionsSection
            sessions={sessions}
            setSessions={setSessions}
            movies={movies}
            rooms={rooms}
            isAdmin={currentUser.role !== "USER"}
            selectedSession={selectedSession}
            setSelectedSession={setSelectedSession}
            setTakenSeats={setTakenSeats}
            setShowSeatModal={setShowSeatModal}
          />
        )}

        {section === "TICKETS" && (
          <TicketsSection
            sessions={sessions}
            selectedSession={selectedSession}
            setSelectedSession={setSelectedSession}
            takenSeats={takenSeats}
            setTakenSeats={setTakenSeats}
          />
        )}

        {section === "STARWARS" && currentUser.role !== "USER" && (
          <StarWarsSection movies={movies} setMovies={setMovies} />
        )}

        {section === "USERS" && currentUser.role === "ADMIN" && (
          <UsersSection />
        )}

        {/* Widget de chat accesible en toda la aplicación */}
        {currentUser && <ChatWidget user={currentUser} />}
      </main>
    </div>
  );
}

export default App;
