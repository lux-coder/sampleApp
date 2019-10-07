import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../model/user';
import { PasswordChange } from '../model/password-change';
import { ServerConstant } from '../constant/server-constant';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constant: ServerConstant = new ServerConstant();
  public host: string = this.constant.host;
  public token: string;
  public loggInUsername: string | null;
  public redirectUrl: string;  
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}

  login(user: User): Observable<HttpErrorResponse | HttpResponse<any>>  {
    return this.http.post<HttpErrorResponse | HttpResponse<any>>(`${this.host}/user/login`, user, { observe: 'response' });
  }

  register(user: User): Observable<User | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/user/register`, user);
  }

  resetPassword(email: string) {
    return this.http.get(`${this.host}/user/resetPassword/${email}`, {
      responseType: 'text'
    });
  }

  getUsers(): Observable<User[]> {
    console.log("Calling for users")
    return this.http.get<User[]>(`${this.host}/user/list`);
  }

  logOut(): void {
    this.token = null;
    localStorage.removeItem('token');
  }

  saveToken(token: string): void {
    this.token = token;
    localStorage.setItem('token', token);
  }

  loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  getToken(): string {
   return this.token;
  }

  isLoggedIn(): boolean {
    this.loadToken();
    if (this.token != null && this.token !== '') {
      if (this.jwtHelper.decodeToken(this.token).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggInUsername = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    } else {
      this.logOut();
      return false;
    }
  }

  getUserInformation(username: string): Observable<User> {
    return this.http.get<User>(`${this.host}/user/${username}`);
  }

  searchUsers(username: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.host}/user/findByUsername/${username}`);
  }

  updateUser(updateUser: User): Observable<User> {
    return this.http.post<User>(`${this.host}/user/update`, updateUser);
  }

  changePassword(changePassword: PasswordChange) {
    return this.http.post(`${this.host}/user/changePassword`, changePassword, {responseType: 'text'});
  }
}
