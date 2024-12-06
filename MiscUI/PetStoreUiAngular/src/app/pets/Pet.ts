export class Pet {

    id: number;
    name: string;
    sex: string;
    vaccinated: string;
    checked: boolean = false;

    constructor(id: number, name: string, sex: string, vaccinated: string) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.vaccinated = vaccinated;
    }

    clear() {
        this.name = "";
        this.sex = "";
        this.vaccinated = "";
    }
}
