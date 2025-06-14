import { combineReducers } from "redux";
import { FIND_PET_BY_ID_REQUEST, FIND_PET_BY_ID_SUCCESS, FIND_PET_BY_ID_ERROR,
    GET_ALL_PETS_REQUEST, GET_ALL_PETS_SUCCESS, GET_ALL_PETS_ERROR} from "../app/api/actions";
import newPetReducer from "./new_pet";

const initialState = {
    petSearchLoading: false,
    petListLoading: false,
    error: null,
};

const petSearchReducer = (state = initialState, action) => {

    switch (action.type) {
        // GET_ALL_PETS
        case GET_ALL_PETS_REQUEST:
            return {... state, petSearchLoading: true, petListLoading: true, error: null, petList: null, clientList: null};
        case GET_ALL_PETS_SUCCESS:
            return {... state, petSearchLoading: false, petListLoading: false, error: null, 
                petList: action.payload.pets, clientList: action.payload.clients };
        case GET_ALL_PETS_ERROR:
            return {... state, petSearchLoading: false, petListLoading: false, error: action.payload };
        
        // FIND_PET_BY_ID
        case FIND_PET_BY_ID_REQUEST:
            return {... state, petSearchLoading: true, petListLoading: true, error: null, petList: null};
        case FIND_PET_BY_ID_SUCCESS:
            return { ...state, petSearchLoading: false, petListLoading: false, error: null, petList: action.payload.pets };
        case FIND_PET_BY_ID_ERROR:
            return { ...state, petSearchLoading: false, petListLoading: false, error: action.payload };
        
        default:
            return state;
    }
}

const reducer = combineReducers({
    petSearch: petSearchReducer,
    newPet: newPetReducer
})

export default reducer;