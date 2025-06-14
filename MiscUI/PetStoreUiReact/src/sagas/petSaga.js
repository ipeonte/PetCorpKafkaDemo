import { takeLatest } from "redux-saga/effects";
import { ADD_NEW_PET_REQUEST, addNewPet, ADOPT_PET_REQUEST, adoptPets, FIND_PET_BY_ID_REQUEST, findPetById, GET_ALL_PETS_REQUEST, getAllPets } from "../app/api/actions";

export function* petSaga() {[
    yield takeLatest(GET_ALL_PETS_REQUEST, getAllPets),
    yield takeLatest(FIND_PET_BY_ID_REQUEST, findPetById),
    yield takeLatest(ADD_NEW_PET_REQUEST, addNewPet),
    yield takeLatest(ADOPT_PET_REQUEST, adoptPets)
  ]
}