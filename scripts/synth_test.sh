#!/bin/bash
PFILE=new_pet.json

# Preinstall client for synthetic test
CLIENT_ID=3

# Login to get SSO cookie
curl -c sso.txt -X POST -d "username=admin&password=password" http://localhost/login

# Add non-vaccinated pet
curl -s -b sso.txt -o - -X POST -H "Content-Type: application/json" -d '{"name":"Test","sex":"Male","vaccinated":"N"}' http://localhost/api/v1/admin/pet?st > $PFILE
if [ ! -f $PFILE ]; then
	echo "New pet file not found"
	exit 1
fi
NEW_ID=`cat $PFILE | jq -r ".id"`
echo "Added new pet with Id:$NEW_ID"

# Check new pet is visible for admin
TEST_ID=`curl -s -b sso.txt -o - http://localhost/api/v1/admin/pet/$NEW_ID?st | jq -r ".id"`
if [ -z "${TEST_ID}" ]; then
	echo "New pet with Id:$NEW_ID not found"
	exit 2
fi

# Set pet vaccinated
VST=`curl -s -b sso.txt -o - -X PUT -H "Content-Type: application/json" -d 'true' http://localhost/api/v1/admin/pet/$NEW_ID/vaccinated?st | jq -r ".vaccinated"`
if [[ "$VST" != "Y" ]]; then
        echo "Vaccinated status for new pet with Id:$NEW_ID didn't changed"
        exit 3
fi
echo "Change vaccinated status to 'Y' for new pet with Id:$NEW_ID"

# Check pet is visible for public
TEST_ID=`curl -s -b sso.txt -o - http://localhost/api/v1/pet/$NEW_ID?st | jq -r ".id"`
if [ -z "${TEST_ID}" ]; then
        echo "New pet with Id:$NEW_ID is not visible for public"
        exit 4
fi
echo "New pet with Id:$NEW_ID now visible for public"

# Adopt pet
curl -s -b sso.txt -o - -X POST http://localhost/api/v1/mgr/pet/$NEW_ID/$CLIENT_ID?st
echo "Adopted pet with Id: $NEW_ID"

# Check pet no longer visible for public
TEST_ID=`curl -s -b sso.txt -o - http://localhost/api/v1/pet/$NEW_ID?st | jq -r ".id"`
if [ ! -z "${TEST_ID}" ]; then
        echo "New pet with Id:$NEW_ID is still visible for public"
        exit 4
fi
echo "New pet with Id:$NEW_ID is no longer visible for public"

# Check client has pet assigned
sleep 1
PET_LIST=`curl -s -b sso.txt -o - http://localhost/api/v1/clients/$CLIENT_ID/pets?st`
if ! [[ "$PET_LIST" =~ $NEW_ID ]]; then
        echo "New pet with Id:$NEW_ID not assigned to client $CLIENT_ID"
        exit 5
fi
echo "New pet with Id:$NEW_ID is assigned to Client:$CLIENT_ID"

# Delete pet synthetic test data
curl -s -b sso.txt -o - -X DELETE http://localhost/api/v1/admin/clear_st
# TODO Delete client synthetic test data
curl -s -b sso.txt -o - -X DELETE http://localhost/api/v1/clients/clear_st

rm sso.txt
rm $PFILE
