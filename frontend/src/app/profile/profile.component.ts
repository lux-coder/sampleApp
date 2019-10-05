import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from '../service/user.service';
import { LoadingService } from '../service/loading.service';
import { AlertService } from '../service/alert.service';
import { User } from '../model/user';
import { PasswordChange } from '../model/password-change';
import { AlertType } from '../enum/alert-type.enum';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];  
  user: User;
  host: string;
  userHost: string;
  postHost: string;
  username: string;  

  constructor(
    private route: ActivatedRoute,
    public userService: UserService,    
    private router: Router,
    private loadingService: LoadingService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.username = this.route.snapshot.paramMap.get('username');
    this.host = this.userService.host;    
    this.getUserInfo(this.username);
    this.loadingService.isLoading.next(false);
  }

  getUserInfo(username: string): void {
    this.subscriptions.push(
      this.userService.getUserInformation(username).subscribe(
        (response: User) => {
          this.user = response;          
        },
        error => {
          console.log(error);
          this.user = null;
        }
      )
    );
  }

  onUpdateUser(updatedUser: User): void {
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.userService.updateUser(updatedUser).subscribe(
        response => {
          console.log(response);          
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert(
            'Profile updated successfully.',
            AlertType.SUCCESS
          );
        },
        error => {
          console.log(error);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert(
            'Profile update failed. Please try again..',
            AlertType.DANGER
          );
        }
      )
    );
  }

  onChangePassword(passwordChange: PasswordChange) {
    console.log(passwordChange);
    const element: HTMLElement = document.getElementById(
      'changePasswordDismiss'
    ) as HTMLElement;
    element.click();
    this.loadingService.isLoading.next(true);
    this.subscriptions.push(
      this.userService.changePassword(passwordChange).subscribe(
        response => {
          console.log(response);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert(
            'Password was updated successfully',
            AlertType.SUCCESS
          );
        },
        error => {
          console.log(error);
          this.loadingService.isLoading.next(false);
          const errorMsg: string = error.error;
          this.showErrorMessage(errorMsg);
        }
      )
    );
  }

  private showErrorMessage(errorMessage: string): void {
    if (errorMessage === 'PasswordNotMatched') {
      this.alertService.showAlert(
        'Passwords do not match. Please try again.',
        AlertType.DANGER
      );
    } else if (errorMessage === 'IncorrectCurrentPassword') {
      this.alertService.showAlert(
        'The current password is incorrect. Please try again.',
        AlertType.DANGER
      );
    } else {
      this.alertService.showAlert(
        'Password change failed. Please try again.',
        AlertType.DANGER
      );
    }
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
