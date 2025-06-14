"use client"

import { Navbar, Nav, Button } from 'react-bootstrap';
import { Container } from 'react-bootstrap';
import { useContext } from 'react';
import UserInfoContext from './UserInfoContext';

function UserInfoComp() {
    const { userInfo } = useContext(UserInfoContext);

    if (!userInfo)
        return (<p>Loading User Info</p>)

    return (
        <header>
            <Navbar expand="md" className="bg-body-tertiary w-100 p5" style={{padding: 0}}>
                <Container>
                    <Navbar.Brand href="/">Demo Pet Store UI</Navbar.Brand>
                    <Container fluid style={{textAlign: 'right'}}>
                        <Navbar.Text>Signed in as [{userInfo.name}]</Navbar.Text>
                    </Container>
                    <Nav>
                        <Nav.Link href="/logout">Logout</Nav.Link>
                    </Nav>
                </Container>
            </Navbar>
        </header>
    );
}

export default UserInfoComp;