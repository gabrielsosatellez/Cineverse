import React, { useEffect, useState } from "react";
import { fetchData, putData } from "../../utils/api";

export default function UsersSection() {
  const [users, setUsers] = useState([]);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [savingId, setSavingId] = useState(null);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [confirmUserId, setConfirmUserId] = useState(null);
  const [confirmNewRole, setConfirmNewRole] = useState(null);
  const [confirmUserName, setConfirmUserName] = useState("");

  // Carga la lista de usuarios desde el backend
  const loadUsers = async () => {
    setLoading(true);
    try {
      const data = await fetchData("http://localhost:8080/api/users");
      setUsers(Array.isArray(data) ? data : []);
    } catch (e) {
      console.error(e);
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  // Solicita confirmación antes de aplicar el cambio de rol
  const requestChangeRole = (userId, newRole, username) => {
    setConfirmUserId(userId);
    setConfirmNewRole(newRole);
    setConfirmUserName(username || "");
    setConfirmOpen(true);
  };

  // Aplica el cambio de rol del usuario seleccionado
  const applyChangeRole = async () => {
    const userId = confirmUserId;
    const newRole = confirmNewRole;
    setConfirmOpen(false);
    setSavingId(userId);

    try {
      const res = await putData(`http://localhost:8080/api/users/${userId}`, { role: newRole });
      if (!res.ok) throw new Error();
      setUsers(prev => prev.map(u => u.id === userId ? { ...u, role: newRole } : u));
    } catch (e) {
      console.error(e);
      alert("Error al actualizar rol");
    } finally {
      setSavingId(null);
      setConfirmUserId(null);
      setConfirmNewRole(null);
      setConfirmUserName("");
    }
  };

  // Filtra los usuarios por correo o nombre
  const filtered = users.filter(u => {
    const q = query.toLowerCase();
    return (
      u.email?.toLowerCase().includes(q) ||
      u.fullName?.toLowerCase().includes(q)
    );
  });

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Gestión de usuarios</h2>

      <div className="mb-4 flex items-center gap-2">
        <input
          value={query}
          onChange={e => setQuery(e.target.value)}
          placeholder="Buscar por usuario, correo o nombre"
          className="border p-2 rounded w-full"
        />
        <button
          onClick={loadUsers}
          className="bg-gray-200 hover:bg-gray-300 px-3 py-2 rounded transition-colors"
        >
          Recargar
        </button>
      </div>

      {loading ? (
        <p>Cargando usuarios...</p>
      ) : (
        <ul className="space-y-2">
          {filtered.map(u => (
            <li
              key={u.id}
              className="bg-white p-3 rounded shadow flex items-center justify-between"
            >
              <div>
                <div className="font-semibold">
                  {u.username} <span className="text-sm text-gray-500">({u.email})</span>
                </div>
                <div className="text-sm text-gray-600">{u.fullName}</div>
              </div>

              <div className="flex items-center gap-2">
                <select
                  value={u.role}
                  onChange={e => requestChangeRole(u.id, e.target.value, u.username)}
                  className="border rounded p-1"
                  disabled={savingId === u.id}
                >
                  <option value="USER">user</option>
                  <option value="EMPLOYEE">employee</option>
                  <option value="ADMIN">admin</option>
                </select>
                {savingId === u.id ? (
                  <span className="text-sm text-gray-500">Guardando...</span>
                ) : null}
              </div>
            </li>
          ))}

          {filtered.length === 0 && (
            <li className="text-gray-500">No se encontraron usuarios</li>
          )}
        </ul>
      )}

      {/* Modal de confirmación para el cambio de rol */}
      {confirmOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-96 shadow-lg">
            <h3 className="text-lg font-bold mb-2">Confirmar cambio de rol</h3>
            <p className="mb-4">
              ¿Cambiar rol de <span className="font-semibold">{confirmUserName}</span> a{" "}
              <span className="font-semibold">{confirmNewRole.toLowerCase()}</span>?
            </p>
            <div className="flex gap-3">
              <button
                className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={() => setConfirmOpen(false)}
              >
                Cancelar
              </button>
              <button
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded w-full transition-colors"
                onClick={applyChangeRole}
              >
                Confirmar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
