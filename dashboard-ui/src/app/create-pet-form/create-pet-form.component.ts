import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UserServiceService} from "../user-service.service";
import {PetServiceService} from "../pet-service.service";

@Component({
  selector: 'app-create-pet-form',
  templateUrl: './create-pet-form.component.html',
  styleUrls: ['./create-pet-form.component.css']
})
export class CreatePetFormComponent {
  @Input() userId: string | undefined;
  @Input() showForm: boolean | undefined;
  @Output() showFormChange = new EventEmitter<boolean>();
  @Output() petCreated: EventEmitter<any> = new EventEmitter();
  petName: string | undefined;
  petType: string = 'CAT';
  petOwner: string | undefined;
  petOwnerId: any | undefined;
  owners: any[] = [];
  showResults: boolean = false;

  constructor(private userService: UserServiceService,
              private petService: PetServiceService) { }

  onInput(event: any) {
    this.userService.getUsers(event).subscribe(
      (data) => {
      this.owners = data.content;
      this.showResults = true;
    },
      (error) => { console.log(error); }
    );
  }

  selectOwner(owner: any) {
    this.petOwner = owner.username;
    this.petOwnerId = owner.id;
    this.showResults = false;
  }

  createPet() {
    this.closeForm();
    const id = this.userId !== undefined ? null : this.petOwnerId
    const body = {
      userId: id,
      name: this.petName,
      type: this.petType
    };
    this.petService.createPet(body).subscribe(
      (data) => { console.log(data); this.petCreated.emit(); },
      (error) => { console.log(error); }
    );
  }

  closeForm() {
    this.showForm = false;
    this.showFormChange.emit();
  }
}
