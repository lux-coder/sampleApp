import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../service/user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private userService: UserService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (req.url.includes(`${this.userService.host}/user/login`)) {
      return next.handle(req);
    }

    if (req.url.includes(`${this.userService.host}/user/register`)) {
      return next.handle(req);
    }

    if (req.url.includes(`${this.userService.host}/user/resetPassword/`)) {
      return next.handle(req);
    }

    this.userService.loadToken();
    const token = this.userService.getToken();
    const request = req.clone({ setHeaders: { Authorization: token } });
    return next.handle(request);
  }

}
