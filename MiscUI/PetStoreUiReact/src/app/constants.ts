export const BASE_URL = process.env.NEXT_PUBLIC_BASE_URL || "";

export const LOGIN_URL = BASE_URL + "/login";

// Get user info on start
export const GET_USER_INFO_URL = BASE_URL + "/user_info";

// Find pet by id
export const FIND_PET_BY_ID_USER_URL = "/api/v1/pet/:id";
export const FIND_PET_BY_ID_ADMIN_URL = "/api/v1/admin/pet/:id";

// Get all pets
export const GET_ALL_PETS_USER_URL = "/api/v1/pets";
export const GET_ALL_PETS_ADMIN_URL = "/api/v1/admin/pets";

// Get all clients
export const GET_ALL_CLIENTS_URL = "/api/v1/clients";

// Save new pet
export const ADD_NEW_PET_URL = "/api/v1/admin/pet";

// Adopt pet
export const ADOPT_PET_URL = "/api/v1/mgr/pet/:pet_id/:client_id";

// Adopt message
export const ADOPT_MSG = "Please confirm adoption of [n] pet[s] by [client_id]";