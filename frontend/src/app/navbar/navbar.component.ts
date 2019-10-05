import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { HttpEventType } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { AlertService } from '../service/alert.service';
import { UserService } from '../service/user.service';
import { LoadingService } from '../service/loading.service';
import { AlertType } from '../enum/alert-type.enum';
import { User } from '../model/user';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  user: User;
  searchedUser: User[];
  host: string;
  userHost: string;  
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  dateOfBirth: Date;
  userLoggedIn: boolean;
  showNavbar: boolean;
  showSuccessAlert: boolean;  
  clientHost: string;
  

  constructor(
    private router: Router,
    private alertService: AlertService,
    private userService: UserService,    
    private loadingService: LoadingService
  ) { }

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.host = this.userService.host;    
    this.showNavbar = true;
    if (this.userService.isLoggedIn()) {
      this.username = this.userService.loggInUsername;
      this.getUserInfo(this.username);      
      this.loadingService.isLoading.next(false);
    } else {
      this.showNavbar = false;
      this.loadingService.isLoading.next(false);
    }
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.userService.getUserInformation(username).subscribe(
      (response: User) => {
        this.user = response;
        this.userLoggedIn = true;
        this.showNavbar = true;
      },
      error => {
        console.log(error);
        this.userLoggedIn = false;
      }
    ));
  }

  onSearchUsers(event) {
    console.log(event);
    const username = event;
    this.subscriptions.push(this.userService.searchUsers(username).subscribe(
      (response: User[]) => {
        console.log(response);
        this.searchedUser = response;
      },
      error => {
        console.log(error);
        return this.searchedUser = [];
      }
    ));
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
  }

  getSearchUserProfile(username: string): void {
    const element: HTMLElement = document.getElementById(
      'closeSearchModal'
    ) as HTMLElement;
    element.click();
    this.router.navigate(['/profile', username]);
    setTimeout(() => {
      location.reload();
    }, 100);
  }

  logOut(): void {
    this.loadingService.isLoading.next(true);
    this.userService.logOut();
    this.router.navigateByUrl('/login');
    this.loadingService.isLoading.next(false);
    this.alertService.showAlert(
      'You have been successfully logged out.',
      AlertType.SUCCESS
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe);
  }
}
