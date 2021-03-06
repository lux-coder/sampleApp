import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from '../service/user.service';
import { LoadingService } from '../service/loading.service';
import { AlertType } from '../enum/alert-type.enum';
import { AlertService } from '../service/alert.service';
import { User } from '../model/user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private userService: UserService,
    private loadingService: LoadingService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    if (this.userService.isLoggedIn()) {
      if (this.userService.redirectUrl) {
        this.router.navigateByUrl(this.userService.redirectUrl);
      } else {
        this.router.navigateByUrl('/home');
      }
    } else {
      this.router.navigateByUrl('/login');
    }
  }

  onLogin(user: User): void {
    this.loadingService.isLoading.next(true);
    console.log(user);
    this.subscriptions.push(
      this.userService.login(user).subscribe(
        response => {
          const token: string = response.headers.get('Authorization');
          this.userService.saveToken(token);
          if (this.userService.redirectUrl) {
            this.router.navigateByUrl(this.userService.redirectUrl);            
          } else {
            this.router.navigate(['/profile/', user.username]);            
          }
          this.loadingService.isLoading.next(false);
        },
        error => {
          console.log(error);
          this.loadingService.isLoading.next(false);
          this.alertService.showAlert(
            'Username or password incorrect. Please try again.',
            AlertType.DANGER
          );
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe);
  }
}
