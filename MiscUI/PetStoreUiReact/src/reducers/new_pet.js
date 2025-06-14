import { ADD_NEW_PET_REQUEST, ADD_NEW_PET_SUCCESS, ADD_NEW_PET_ERROR, RESET_NEW_PET} from "../app/api/actions";

const initialState = {
    isLoading: false,
    error: null,
};

const newPetReducer = (state = initialState, action) => {
    switch (action.type) {
        // ADD_NEW_PET
        case ADD_NEW_PET_REQUEST:
            return {... state, isLoading: true, error: null, pet: null};
        case ADD_NEW_PET_SUCCESS:
            return {... state, isLoading: false, error: null, pet: action.payload};
        case ADD_NEW_PET_ERROR:
            return {... state, isLoading: false, error: action.payload};
        case RESET_NEW_PET:
            return {... state, pet: null};

        default:
            return state;
    }
}

export default newPetReducer;