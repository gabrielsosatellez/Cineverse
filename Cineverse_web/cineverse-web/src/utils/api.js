// Formatea una fecha ISO a formato legible para el usuario (dd/MM/yyyy HH:mm)
export const formatDateTime = (dateTimeString) => {
  const date = new Date(dateTimeString);
  return date.toLocaleString("es-ES", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
};

// Comprueba si una película ya existe en el catálogo por título
export const movieExists = (title, movies) => {
  return movies.some(m => m.title.toLowerCase() === title.toLowerCase());
};

// Realiza una petición GET y devuelve la respuesta en JSON
export const fetchData = async (url) => {
  const response = await fetch(url);
  return response.json();
};

// Realiza una petición POST al backend
export const postData = async (url, data) => {
  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  return response;
};

// Realiza una petición DELETE al backend
export const deleteData = async (url) => {
  return fetch(url, { method: "DELETE" });
};

// Realiza una petición PUT al backend
export const putData = async (url, data) => {
  const response = await fetch(url, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  return response;
};
