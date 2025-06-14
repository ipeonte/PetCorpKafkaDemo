"use client"
import { Fragment, useContext, useEffect, useRef, useState } from "react";
import { Button, FloatingLabel, FormSelect, Modal, Spinner, Table } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import UserInfoContext from "../user_info/UserInfoContext";
import { addNewPetRequest, adoptPetsRequest, adoptPetsSuccess, getAllPetsRequest, resetNewPet } from "../../api/actions";
import FormCheckInput from "react-bootstrap/esm/FormCheckInput";
import { ADOPT_MSG } from "../../constants";

// List of checked fields
const checkedPetList = new Map();

function ClientSelect({clients, onPetAdopt, isPetChecked}) {
    const [client, setClient] = useState();

    function handleClientChange(e) {
        const val = e.target.value
        setClient(val);
    }

    function handlePetAdopt() {
        onPetAdopt(client);
    }
    
    return (
        <Fragment>
            <FormSelect value={client} className="w-auto mb-3" name="client" onChange={handleClientChange}>
                <option>Select Client</option>
                {clients.map((client) => (
                    <option key = {client.id} value={client.id}>{client.first_name + " " + client.last_name}</option>
                ))}
            </FormSelect>
            <Button variant="danger" disabled={!client || !isPetChecked} onClick={handlePetAdopt}>
                Adopt Selected
            </Button>
        </Fragment>
    )
}

export default function PetList({data}) {
    const dispatch = useDispatch(); 
    const { userInfo } = useContext(UserInfoContext);
    const [ errMsg, setErrMsg ] = useState();
    const [ petList, setPetList ] = useState();
    const [ petListLoading, setPetListLoading ] = useState(false);
    const [ isModal, setModal ] = useState(false);

    const emptyPet = {name: "Jira", sex: "Female", vaccinated: "N"};
    const emptyClient = "";
    const [ newPet, setNewPet ] = useState(emptyPet);
    const [ clientList, setClientList ] = useState();
    const [ newPetLoading, setNewPetLoading ] = useState(false);
    const [ isPetChecked, setPetChecked ] = useState(false);

    useEffect(() => {
        // Initiate chain request on pet list render after
        // user info loaded
        if (userInfo && !petListLoading && !errMsg) {
            refreshAllPets();
        }
    }, [userInfo]);

    useSelector((state) => {
        const ps = state.petSearch;
        if (ps.petListLoading != petListLoading) {
            setPetListLoading(ps.petListLoading);
        } else if (ps.error !== errMsg) {
            setErrMsg(ps.error);
        } else if (ps.petList !== petList) {
            // Pet List refreshed
            setPetList(ps.petList);
            setClientList(ps.clientList);
            checkedPetList.clear();
            setPetChecked(false);
        }

        const np = state.newPet;
        if (np.isLoading !== newPetLoading)
            setNewPetLoading(state.newPet.isLoading);
        else if (np.pet) {
            if (isModal) {
                // New pet just saved.
                // Use new pet id as internal state variable to refresh pet list
                // after modal exited
                setNewPet(prevPet => ({...prevPet, id: np.pet.id}));
                setModal(false);
                dispatch(resetNewPet());
            }
        }
    });

    function refreshAllPets() {
        dispatch(getAllPetsRequest(userInfo.mask));
    }

    function showAddPetform() {
        setModal(true);
        setNewPet(emptyPet);
    }

    function handleNewPetFormClose() {
        setModal(false);
    }

    function handleNewPetFormSave() {
        dispatch(addNewPetRequest(newPet));
    }

    function setNewPetName(e) {
        setNewPet(prevPet => ({...prevPet, name: e.target.value}));
    }

    function setNewPetSex(e) {
        setNewPet(prevPet => ({...prevPet, sex: e.target.value}));
    }

    function setNewPetVaccinated(e) {
        setNewPet(prevPet => ({...prevPet, vaccinated: e.target.value}));
    }

    function newPetReady() {
        return newPet.name && newPet.sex && newPet.vaccinated;
    }

    function closeModal() {
        if (newPet.id) {
            // Reset new pet state
            setNewPet(emptyPet);

            // Refresh pet list
            refreshAllPets();
        }
    }

    function adoptPet(client) {
        const pl = [];
        // Get the list of checked pets
        for(const idx of checkedPetList.keys()) {
            checkedPetList.get(idx) && pl.push(idx);
        }

        if (!confirm(ADOPT_MSG
                .replace("[n]", pl.length)
                .replace("[s]", pl.length == 1 ? "" : "s")
                .replace("[client_id]", client)))
            return;

        console.log("Adopting Pet for client -> pets", client, pl);
        dispatch(adoptPetsRequest(client, pl, userInfo.mask));
    }

    function handlePetChecked(e) {
        checkedPetList.set(e.target.id, e.target.checked);

        for (const val of checkedPetList.values()) {
            if (val) {
                setPetChecked(true);
                return;
            }
        }

        setPetChecked(false);
    }

    // Quick check
    if (!userInfo)
        return null;

    // Max row tab size
    let tabSize = 3;

    // Tab size for button group
    let btnTabSize = 4;

    const roleUser = userInfo.mask === 0;
    const rolePetKeeper = (userInfo.mask & 1) === 1;
    const roleManager = (userInfo.mask & 2) === 2;

    if (userInfo.mask > 0) {
        if (rolePetKeeper)
            // Pet handler can add pet
            tabSize += 1;

        if (roleManager)
            // Manager can adopt pet
            tabSize += 1;
    }

    return (
        <Fragment>
        <Table striped bordered hover className="w-50">
            <thead>
                <tr>
                    <th className="pet-id">#</th>
                    <th className="pet-name">Name</th>
                    <th className="pet-sex">Sex</th>
                    { rolePetKeeper && <th className="pet-vaccinated">Vaccinated</th>}
                    { roleManager && <th className="pet-action">Selection</th>}
                </tr>
            </thead>
            <tbody>
                {
                    petListLoading ?
                     <tr><td colSpan={tabSize} className="center">
                        <Spinner style={{verticalAlign: "middle"}} />
                        </td>
                     </tr> :
                    (
                        petList && petList.length > 0 ?
                        [petList.map((rec, idx) => (
                            <tr key={idx}>
                                <td>{rec.id}</td>
                                <td>{rec.name}</td>
                                <td>{rec.sex}</td>
                                { rolePetKeeper && <td>{rec.vaccinated === "Y" 
                                    ? "Yes" 
                                    : (rec.vaccinated === "N" ? "No" : rec.vaccinated)}</td>
                                }
                                { 
                                    roleManager && 
                                    <td className="center">
                                        <FormCheckInput onChange={handlePetChecked} id={rec.id} />
                                    </td>
                                }
                            </tr>
                        )),
                        !roleUser && <tr key="btn_block">
                            {
                                rolePetKeeper && 
                                <td colSpan={btnTabSize} className="center align-middle">
                                    <button id="add_pet" className="btn btn-primary" onClick={showAddPetform}>Add Pet</button>
                                </td>
                            }
                            {
                                roleManager && <td className="text-center">
                                    <ClientSelect clients={clientList} onPetAdopt={adoptPet} isPetChecked={isPetChecked}></ClientSelect>
                                    </td>
                            }
                            </tr>] : (
                           <tr><td colSpan={tabSize} className="center">No pets found</td></tr> 
                        )
                    )
                }
            </tbody>
        </Table>
        <Modal show={isModal} onHide={handleNewPetFormClose} onExited={closeModal}>
            <Modal.Header closeButton />
            <Modal.Title className="text-center">New Pet Form</Modal.Title>
            <Modal.Body>
                <div className="form-group">
                    <label>Pet Name</label>
                    <input type="text" className="form-control" 
                        placeholder="Enter Pet Name" value={newPet.name} onChange={setNewPetName} />
                </div>
                <div className="form-group">
                    <label>Select Pet Sex</label>
                    <select className="form-control" value={newPet.sex} onChange={setNewPetSex} >
                        <option value=""></option>
                        <option value="Male">Male</option>
                        <option value="Female">Femail</option>
                    </select>
                </div>
                <div className="form-group">
                    <label>Is Vaccinated ?</label>
                    <select className="form-control" value={newPet.vaccinated} onChange={setNewPetVaccinated} >
                        <option value=""></option>
                        <option value="Y">Yes</option>
                        <option value="N">No</option>
                    </select>
                </div>
            </Modal.Body>
            <Modal.Footer>
                {newPetLoading
                    ? <Spinner></Spinner>
                    : <Fragment>
                        <Button variant="secondary" onClick={handleNewPetFormClose}>Close</Button>
                        <Button variant="primary" onClick={handleNewPetFormSave}
                                                disabled={!newPetReady()}>Save Changes</Button>
                    </Fragment>
                }
            </Modal.Footer>
        </Modal>
        </Fragment>
    );
}