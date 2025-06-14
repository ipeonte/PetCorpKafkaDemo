"use client"
import { getAllPetsRequest, findPetByIdRequest } from "../../api/actions";
import { useContext, useEffect, useRef, useState } from "react";
import { Button, Spinner } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import UserInfoContext from "../user_info/UserInfoContext";

function PetSearch() {
    const { userInfo } = useContext(UserInfoContext);
    const petIdRef = useRef(null);
    const dispatch = useDispatch();
    const [petId, setPetId] = useState("");
    const [errMsg, setErrMsg] = useState();
    const [petSearchLoading, setPetSearchLoading] = useState();

    useSelector((state) => {
        const ps = state.petSearch;

        if (ps.petSearchLoading != petSearchLoading) {
            setPetSearchLoading(ps.petSearchLoading);
            if (ps.petSearchLoading)
                console.log("Disable find button");
        } 
        
        if (ps.error !== errMsg) {
            if (ps.error)
                console.log("Setting error message", ps.error);
            setErrMsg(ps.error);
        }
    });

    function handlePetSearchChange(e) {
        setPetId(e.target.value);
    }
    
    function findPetById() {
        console.log("Search for pet id: ", petId);
        dispatch(findPetByIdRequest(petId, userInfo.mask));
    }

    function getAllPets() {
        // Reset pets
        console.log("Get all pets");
        setPetId("");
        dispatch(getAllPetsRequest(userInfo.mask));
    }

    // Quick check
    if (!userInfo)
        return null;

    return (
        <div className="po-container pb-2">
            <div className="form-group mr-3 mb-2" style={{display: "inline-block"}}>
                <input type="number" autoFocus className="form-control" 
                    ref={petIdRef} placeholder="Enter Pet Id" value={petId} onChange={handlePetSearchChange} />
            </div>
            {
                petSearchLoading ?
                    <Spinner animation="border"  style={{verticalAlign: "middle"}} /> :
                    <div className="btn-group">
                        <Button variant="primary" onClick={findPetById} disabled={!petId}>
                            Find
                        </Button>
                        <Button variant="secondary" onClick={getAllPets} style={{marginLeft: "10px"}}>
                            Reset
                        </Button>
                    </div>
            }
            {
                 errMsg && <div className="error">{errMsg}</div>
            }
        </div>
    )
}

export default PetSearch;
