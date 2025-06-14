import axios from "axios";
import { ADD_NEW_PET_URL, ADOPT_PET_URL, FIND_PET_BY_ID_ADMIN_URL, FIND_PET_BY_ID_USER_URL,
    GET_ALL_CLIENTS_URL,
    GET_ALL_PETS_ADMIN_URL, GET_ALL_PETS_USER_URL } from "../constants";
import { checkApiError } from "./common";
import { call, put } from "redux-saga/effects";

// Get All Pets
export const GET_ALL_PETS_REQUEST = "GET_ALL_PETS_REQUEST";
export const GET_ALL_PETS_SUCCESS = "GET_ALL_PETS_SUCCESS";
export const GET_ALL_PETS_ERROR = "GET_ALL_PETS_ERROR";

// Find Pet By Id
export const FIND_PET_BY_ID_REQUEST = "FIND_PET_REQUEST";
export const FIND_PET_BY_ID_SUCCESS = "FIND_PET_SUCCESS";
export const FIND_PET_BY_ID_ERROR = "FIND_PET_ERROR";

// Add new pet
export const ADD_NEW_PET_REQUEST = "ADD_NEW_PET_REQUEST";
export const ADD_NEW_PET_SUCCESS = "ADD_NEW_PET_SUCCESS";
export const ADD_NEW_PET_ERROR = "ADD_NEW_PET_ERROR";
export const RESET_NEW_PET = "RESET_NEW_PET";

// Adopt pet
export const ADOPT_PET_REQUEST = "ADOPT_PET_REQUEST";
export const ADOPT_PET_ERROR = "ADOPT_PET_ERROR";

// Get All Pets Actions
export function getAllPetsRequest(mask) {
    return { type: GET_ALL_PETS_REQUEST, payload: {mask: mask} };
}

export function getAllPetsSuccess(data) {
    return { type: GET_ALL_PETS_SUCCESS, payload: data };
}

export function getAllPetsError(error) {
    return { type: GET_ALL_PETS_ERROR, payload: error };
}

// Find Pet By Id Actions
export function findPetByIdRequest(petId, mask) {
    return { type: FIND_PET_BY_ID_REQUEST, payload: {petId: petId, mask: mask} };
}

export function findPetByIdSuccess(pet) {
    return { type: FIND_PET_BY_ID_SUCCESS, payload: pet };
}

export function findPetByIdError(error) {
    return { type: FIND_PET_BY_ID_ERROR, payload: error };
}

// Add new pet Actions
export function addNewPetRequest(pet) {
    return { type: ADD_NEW_PET_REQUEST, payload: {pet: pet} };
}

export function addNewPetSuccess(pet) {
    return { type: ADD_NEW_PET_SUCCESS, payload: pet };
}

export function addNewPetError(error) {
    return { type: ADD_NEW_PET_ERROR, payload: error };
}

export function resetNewPet() {
    return { type: RESET_NEW_PET };
}

// Adopt pet
export function adoptPetsRequest(client, pets, mask) {
    return { type: ADOPT_PET_REQUEST, payload: {client: client, pets: pets, mask: mask}};
}

export function adoptPetsSuccess(mask) {
    console.log("adoptPetsSuccess", mask);
    return getAllPetsRequest(mask);
}

export function adoptPetsError(error) {
    return { type: ADOPT_PET_ERROR, payload: error };
}

const findPetByIdEx = async ({petId, mask}) => {
    const response = await axios.get((mask == 0 
        ? FIND_PET_BY_ID_USER_URL 
        : FIND_PET_BY_ID_ADMIN_URL).replace(":id", petId));
    console.log("findPetByIdEx Data", response.data);
    return { pets: response.data ? [response.data] : [] };
}

const getPets = async (mask) => {
    return axios.get(mask === 0 ? GET_ALL_PETS_USER_URL : GET_ALL_PETS_ADMIN_URL);
}

const getClients = async () => {
    return axios.get(GET_ALL_CLIENTS_URL);
}

const getAllPetsEx = async (mask) => {
    const response = await getPets(mask);
    console.log("getPets", response.data);
    return { pets: response.data };
}

const getAllPetsClients = async (mask) => {
    const response = await Promise.all([getPets(mask), getClients()]);
    console.log("getPetsClients", response);
    return {pets: response[0].data, clients: response[1].data};
}

const getInitData = async (mask) => {
    console.log("getInitData mask",  mask);

    // Based on mask load either only pets or clients as well
    return (mask & 2) === 2 ? getAllPetsClients() : getAllPetsEx(mask);
}

const adoptPetsEx = async ({client, pets}) => {
    console.log("Adopt client -> pets", client, pets);
    const requests = [];
    const url = ADOPT_PET_URL.replace(":client_id", client);
    for (let pet of pets) {
        requests.push(axios.post(url.replace(":pet_id", pet)));
    }

    await Promise.all(requests);
}

/**
 * Add new pet
 * Payload example {"name":"Rex","vaccinated":"Y","sex":"Male"}
 * @param {} pet 
 * @returns new pet
 */
const addNewPetEx = async (newPet) => {
    const response = await axios.post(ADD_NEW_PET_URL, newPet);
    const pet = response.data;
    console.log("New Pet Added", pet);
    return pet;
}

export function* getAllPets({payload}) {
    console.log("Get all pets for mask: ", payload.mask);

    try {
        const pets = yield call(getInitData, payload.mask);
        yield put(getAllPetsSuccess(pets));
    } catch (error) {
        yield put(getAllPetsError(checkApiError(error)));
    }
}

export function* findPetById({payload}) {
    console.log("Searching for pet", payload);

    try {
        const pets = yield call(findPetByIdEx, payload);
        yield put(findPetByIdSuccess(pets));
    } catch(error) {
        yield put(findPetByIdError(checkApiError(error)));
    }
}

export function* addNewPet({payload}) {
    try {
        const pet = yield call(addNewPetEx, payload.pet);
        yield put(addNewPetSuccess(pet));
    } catch(error) {
        yield put(addNewPetError(checkApiError(error)));
    }
}

export function* adoptPets({payload}) {
    try {
        yield call(adoptPetsEx, payload);
        yield put(adoptPetsSuccess(payload.mask));
    } catch(error) {
        yield put(adoptPetsError(checkApiError(error)));
    }
}
