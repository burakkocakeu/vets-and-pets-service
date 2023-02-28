import {Component, OnInit, ViewChild} from '@angular/core';
import { UserServiceService } from '../user-service.service';
import {AuthService} from "../auth.service";
import {PetServiceService} from "../pet-service.service";
import {LoginComponent} from "../login/login.component";
import {Router} from "@angular/router";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  latestUsers: number = 0;
  totalUsers: number = 0;
  totalPets: number = 0;
  userList: any[] = [];
  petList: any[] = [];
  petStatistics: any[] = [];
  petPage: any = {'pageNumber': undefined, 'totalPages': undefined, 'size': undefined};
  isCreateUser = false;

  // Index of the row being edited, -1 if not editing
  editPetIndex = -1;
  // Pet object for edit mode
  editingPet = null;
  deletingPet = null;

  showPetDeleteConfirm = false;
  showCreateUserForm = false;
  showCreatePetForm = false;

  loginComponent: LoginComponent;

  constructor(private userService: UserServiceService,
              private petService: PetServiceService,
              private authService: AuthService,
              private router: Router,
              private modalService: NgbModal) {
    this.loginComponent = new LoginComponent(authService, router, modalService);
  }

  ngOnInit() {
    if (localStorage.getItem('access_token')) {
      this.pageableGetPets();
      if (this.getRole() === 'ADMIN') {
        this.userService.getAllUsers().subscribe(
          (data) => { this.totalUsers = data.content.length; },
          (error) => { console.log(error); }
        )
        this.petService.getPetStatistics().subscribe(
          (data) => {
            this.petStatistics = data;
            this.totalPets = this.petStatistics.reduce((total, current) => total + current.total, 0); },
          (error) => { console.log(error); }
        );
        this.userService.getUserStatistics().subscribe(
          (data) => {
            this.userList = data;
            this.latestUsers = data.length; },
          (error) => { console.log(error); }
        );
      }
    } else {
      window.location.reload();
    }
  }

  pageableGetPets(page: any = undefined) {
    const pageNumber = page !== undefined ? page : this.petPage.pageNumber;
    this.petService.getPets(pageNumber, this.petPage.size).subscribe(
      (data) => { this.petList = data.content;
        this.petPage.pageNumber = data.pageable.pageNumber;
        this.petPage.totalPages = data.totalPages;
        this.petPage.size = 20; },
      (error) => { console.log(error); }
    );
  }

  updateData() {
    this.ngOnInit();
  }

  showUserForm() {
    this.showCreateUserForm = true;
  }

  showPetForm() {
    this.showCreatePetForm = true;
  }

  isCreate() {
    return this.isCreateUser;
  }

  getUserId() {
    if (this.getRole() === 'USER')
      return this.authService.getUserId();
    else
      return undefined;
  }

  getRole() {
    return this.authService.getRole();
  }

  onUserCreated(body: any) {
    this.loginComponent.onRegister(body);
  }

  onUserUpdatedOrCreated() {
    window.location.reload();
  }

  // Method to enter edit mode for a pet
  editPet(index: number, pet: any) {
    this.editPetIndex = index;
    this.editingPet = { ...pet };
  }

  // Method to update a pet
  updatePet(index: number, pet: any) {
    if (this.getRole() === 'ADMIN')
      pet.userId = pet.ownerId;
    // Update the pet on the backend
    this.petService.updatePet(pet.id, pet).subscribe(
      (data) => {
        console.log(data);
        // Update the pet in the local list
        this.petList[index] = { ...pet };

        // Exit edit mode
        this.editPetIndex = -1;
        this.editingPet = null;
      }, (error) => {
        // Handle error
        console.log(error);
    });
  }

  showPetDeleteConfirmDialog(index: number, pet: any) {
    // show the modal dialog
    this.showPetDeleteConfirm = true;
    this.deletingPet = pet;
  }

  onPetDeleteConfirmed(confirmed: boolean) {
    // hide the modal dialog
    this.showPetDeleteConfirm = false;
    if (confirmed) {
      // call the deletePet() method with the index of the pet to delete
      this.deletePet(this.deletingPet);
    }
  }

  deletePet(pet: any) {
    this.petService.deletePet(pet).subscribe(
      (data) => { console.log(data); this.updateData(); },
      (error) => { console.log(error); }
    );
  }

  createUser() {
    this.isCreateUser = true;
    this.showUserForm();
  }
  editUser() {
    this.isCreateUser = false;
    this.showUserForm();
  }

  logout() {
    this.loginComponent.onLogout();
    this.router.navigate(['/']);
  }
}

