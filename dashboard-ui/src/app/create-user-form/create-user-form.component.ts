import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UserServiceService} from "../user-service.service";

@Component({
  selector: 'app-create-user-form',
  templateUrl: './create-user-form.component.html',
  styleUrls: ['./create-user-form.component.css']
})
export class CreateUserFormComponent implements OnInit {
  @Input() showForm: boolean | undefined;
  @Input() isCreate: boolean | undefined; // or Update user
  @Output() showFormChange = new EventEmitter<boolean>();
  @Output() userCreated = new EventEmitter<any>();
  @Output() userUpdatedOrDeleted = new EventEmitter<any>();
  fullName: string | undefined;
  username: string | undefined;
  email: string | undefined;
  password: string | undefined;
  role: string | undefined;
  owner: string | undefined;
  ownerId: any | undefined;
  owners: any[] = [];
  showResults: boolean = false;
  showUserDeleteConfirm = false;

  constructor(private userService: UserServiceService) { }

  ngOnInit(): void {
  }

  resetForm() {
    this.fullName = undefined;
    this.username = undefined;
    this.email = undefined;
    this.password = undefined;
    this.role = undefined;
    this.owner = undefined;
    this.ownerId = undefined;
    this.owners = [];
    this.showResults = false;
    this.showUserDeleteConfirm = false;
  }

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
    this.owner = owner.username;
    this.ownerId = owner.id;

    this.username = owner.username;
    this.fullName = owner.fullName;
    this.email = owner.email;
    this.role = owner.role;

    this.showResults = false;
  }

  createUser() {
    this.closeForm();
    const body = {
      fullName: this.fullName,
      username: this.username,
      email: this.email,
      password: this.password,
      role: this.role
    };
    this.userCreated.emit(body); // Emit the event
    this.resetForm();
  }

  updateUser() {
    const body = {
      id: this.ownerId,
      fullName: this.fullName,
      email: this.email,
      role: this.role
    };
    this.userService.updateUser(body).subscribe(
      (data) => {
        console.log(data);
        this.userUpdatedOrDeleted.emit(); // Emit the event
        this.resetForm();
      }, (error) => {
        console.log(error);
      });
  }

  deleteUser() {
    console.log(this.ownerId);
    this.userService.deleteUser(this.ownerId).subscribe(
      (data) => {
        console.log(data);
        this.userUpdatedOrDeleted.emit(); // Emit the event
        this.resetForm();
      },
      (error) => { console.log(error); }
    );
  }

  showUserDeleteConfirmDialog() {
    // show the modal dialog
    this.showUserDeleteConfirm = true;
  }

  onUserDeleteConfirmed(confirmed: boolean) {
    // hide the modal dialog
    this.showUserDeleteConfirm = false;
    if (confirmed) {
      // call the deleteUser() method with the index of the user to delete
      this.deleteUser();
    }
  }

  closeForm() {
    this.owner = undefined;
    this.ownerId = undefined;
    this.showForm = false;
    this.showFormChange.emit();
    setTimeout(() => {
      this.resetForm();
    },500);
  }
}
