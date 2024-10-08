import { Injectable, inject, Inject, PLATFORM_ID } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private oAuthService = inject(OAuthService);
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
    this.initConfiguration();
  }

  initConfiguration() {
    if (this.isBrowser) {
      const authConfig: AuthConfig = {
        issuer: 'https://accounts.google.com',
        strictDiscoveryDocumentValidation: false,
        clientId: environment.clientId,
        redirectUri: window.location.origin + '/banks',
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
      this.oAuthService.setupAutomaticSilentRefresh();
    }
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
}
