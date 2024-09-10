import { Injectable, inject, Inject, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private oAuthService = inject(OAuthService);
  private router = inject(Router);
  private isBrowser: boolean;

  private isLoggedInSubject = new BehaviorSubject<boolean>(
    this.checkLoginStatus()
  );
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    this.initConfiguration();

    this.oAuthService.events.subscribe((e) => {
      console.log(e);
      if (e.type === 'token_received') {
        this.isLoggedInSubject.next(true);
      } else if (e.type === 'logout') {
        this.isLoggedInSubject.next(false);
      }
    });

    this.checkUserInLocalStorage();
  }

  initConfiguration() {
    if (this.isBrowser) {
      const authConfig: AuthConfig = {
        issuer: 'https://accounts.google.com',
        strictDiscoveryDocumentValidation: false,
        clientId:
          '718430921035-af8gptn4knlce41vpeu60qf3d4bl84oi.apps.googleusercontent.com',
        redirectUri: window.location.origin + '/login',
        scope: 'profile email openid',
        responseType: 'id_token token',
        silentRefreshRedirectUri:
          window.location.origin + '/silent-refresh.html',
        useSilentRefresh: true,
        sessionChecksEnabled: true,
        showDebugInformation: true,
      };

      this.oAuthService.configure(authConfig);

      this.oAuthService.loadDiscoveryDocumentAndTryLogin();
    }

    this.oAuthService.setupAutomaticSilentRefresh();
  }

  private checkLoginStatus(): boolean {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem('id_token');
    }
    return false;
  }

  login() {
    if (this.isBrowser) {
      this.oAuthService.initImplicitFlow();
    }
  }

  logout() {
    if (this.isBrowser) {
      this.oAuthService.revokeTokenAndLogout();
      this.oAuthService.logOut();
    }
  }

  getProfile() {
    const profile = this.oAuthService.getIdentityClaims();
    console.log(profile);
    return profile;
  }

  getToken() {
    if (this.isBrowser) {
      return this.oAuthService.getIdToken();
    }
    return null;
  }

  isLoggedIn() {
    if (this.isBrowser) {
      return this.oAuthService.hasValidIdToken();
    }
    return false;
  }

  private checkUserInLocalStorage(): void {
    if (this.isBrowser) {
      const user = localStorage.getItem('user');
      if (user) {
        this.isLoggedInSubject.next(true);
      } else {
        this.isLoggedInSubject.next(false);
      }
    }
  }
}
