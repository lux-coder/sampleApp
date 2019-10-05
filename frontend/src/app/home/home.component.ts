import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { LoadingService } from '../service/loading.service';
import { AlertService } from '../service/alert.service';
import { Subscription } from 'rxjs';
import { AlertType } from '../enum/alert-type.enum';
import { User } from '../model/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  user = new User();  
  host: string;
  userHost: string;  

  constructor(
    private router: Router,
    private userService: UserService,    
    private loadingService: LoadingService,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.loadingService.isLoading.next(true);
    this.getUserInfo(this.userService.loggInUsername);    
    this.host = this.userService.host;    
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
        this.logOut();
        this.router.navigateByUrl('/login');
      }
    ));
  }

  logOut(): void {
    this.userService.logOut();
    this.router.navigateByUrl('/login');
    this.alertService.showAlert(
      'You need to log in to access this page.',
      AlertType.DANGER
    );
  }

  getUserProfile(username: string): void {
    this.router.navigate(['/profile', username]);
    console.log(username);
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}
