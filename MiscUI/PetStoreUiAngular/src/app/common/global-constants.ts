import { HttpHeaders } from "@angular/common/http";

export class Constants {
    
    public static readonly BASE_URL = "http://localhost";
    public static readonly STORE_PORT = ":8080";
    public static readonly OFFICE_PORT = ":8081";
    public static readonly API_REFIX = "/api/v1";

    public static readonly BASE_STORE_URL = Constants.BASE_URL + Constants.STORE_PORT + Constants.API_REFIX;
    public static readonly BASE_OFFICE_URL = Constants.BASE_URL + Constants.OFFICE_PORT + Constants.API_REFIX;

    public static readonly PETS_URL = Constants.BASE_STORE_URL + "/pets";
    public static readonly PETS_ADMIN_URL = Constants.BASE_STORE_URL + "/admin/pets";
    public static readonly CLIENTS_URL = Constants.BASE_OFFICE_URL + "/clients";

    public static readonly PET_FIND_URL = Constants.BASE_STORE_URL + "/pet";
    public static readonly PET_ADMIN_URL = Constants.BASE_STORE_URL + "/admin/pet";

    public static readonly MGR_ADOPT_PET = Constants.BASE_STORE_URL + "/mgr/pet/";

    public static readonly CORS_HEADER = {
        headers: new HttpHeaders({ 
          'Access-Control-Allow-Origin': 'http://localhost:4200'
        })
      };
}