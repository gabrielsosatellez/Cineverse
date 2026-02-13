import React, { useState } from "react";

export default function Sidebar({ isOpen, onSectionChange, onLogout, userRole }) {

    // Gestiona el cambio de sección en el panel principal
    const handleNavigation = (section) => {
        onSectionChange(section);
    };

    const [deleting, setDeleting] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    // Abre el modal de confirmación para eliminar la cuenta
    const handleDeleteAccount = () => {
        setConfirmOpen(true);
    };

    // Ejecuta la eliminación de la cuenta del usuario autenticado
    const handleConfirmDelete = async () => {
        setDeleting(true);
        setConfirmOpen(false);

        let user = null;
        try {
            user = JSON.parse(localStorage.getItem("currentUser"));
        } catch (e) {
            user = null;
        }

        // Validación básica del usuario antes de realizar la petición
        if (!user?.id) {
            alert("No se pudo identificar al usuario");
            setDeleting(false);
            return;
        }

        try {
            const headers = {};
            if (user?.token) {
                headers["Authorization"] = `Bearer ${user.token}`;
            }

            // Petición al backend para eliminar la cuenta del usuario
            const res = await fetch(`http://localhost:8080/api/users/${user.id}`, {
                method: "DELETE",
                headers
            });

            if (!res.ok) {
                const errorBody = await res.json().catch(() => ({}));
                throw new Error(errorBody.message || "Error al eliminar la cuenta");
            }

            alert("Cuenta eliminada correctamente");
            onLogout();
        } catch (e) {
            alert(e.message || "Error al eliminar la cuenta");
        } finally {
            setDeleting(false);
        }
    };

    return (
        isOpen && (
            <aside className="fixed top-0 left-0 h-full w-64 bg-gray-800 text-white p-4 z-50">
                <h2 className="text-xl font-bold mb-6">CineVerse</h2>

                <div
                    className="p-2 hover:bg-gray-700 cursor-pointer"
                    onClick={() => handleNavigation("MOVIES")}
                >
                    Películas
                </div>

                <div
                    className="p-2 hover:bg-gray-700 cursor-pointer"
                    onClick={() => handleNavigation("ROOMS")}
                >
                    Salas
                </div>

                <div
                    className="p-2 hover:bg-gray-700 cursor-pointer"
                    onClick={() => handleNavigation("SESSIONS")}
                >
                    Sesiones
                </div>

                <div
                    className="p-2 hover:bg-gray-700 cursor-pointer"
                    onClick={() => handleNavigation("TICKETS")}
                >
                    Entradas
                </div>

                {/* Secciones visibles solo para empleados y administradores */}
                {userRole !== "USER" && (
                    <div
                        className="p-2 hover:bg-gray-700 cursor-pointer"
                        onClick={() => handleNavigation("STARWARS")}
                    >
                        Star Wars
                    </div>
                )}

                {/* Sección visible solo para administradores */}
                {userRole === "ADMIN" && (
                    <div
                        className="p-2 hover:bg-gray-700 cursor-pointer"
                        onClick={() => handleNavigation("USERS")}
                    >
                        Gestionar usuarios
                    </div>
                )}

                {/* Acción de cierre de sesión */}
                <div
                    className="p-2 bg-red-600 hover:bg-red-700 mt-6 cursor-pointer transition-colors"
                    onClick={onLogout}
                >
                    Cerrar sesión
                </div>

                {/* Acción de eliminación de cuenta */}
                <div
                    className="p-2 bg-red-600 hover:bg-red-700 mt-2 cursor-pointer transition-colors"
                    onClick={handleDeleteAccount}
                >
                    Eliminar cuenta
                </div>

                {/* Modal de confirmación para eliminar la cuenta */}
                {confirmOpen && (
                    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
                        <div className="bg-white rounded-lg p-6 w-96 shadow-lg">
                            <h3 className="text-lg font-bold mb-2 text-gray-900">
                                Confirmar eliminación
                            </h3>
                            <p className="mb-4 text-gray-700">
                                ¿Estás seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.
                            </p>
                            <div className="flex gap-3">
                                <button
                                    className="bg-gray-200 hover:bg-gray-300 px-4 py-2 rounded w-full"
                                    onClick={() => setConfirmOpen(false)}
                                >
                                    Cancelar
                                </button>
                                <button
                                    className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded w-full"
                                    onClick={handleConfirmDelete}
                                >
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </aside>
        )
    );
}
