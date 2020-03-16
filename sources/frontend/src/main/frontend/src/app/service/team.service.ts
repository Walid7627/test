import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { UrlConfig } from '../core/config/url-config';

import 'rxjs/add/operator/map';
import { Team } from '../../model/team.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TeamService {

  allTeamUrl = UrlConfig.API_URL + "/api/teams";


  constructor(private http: HttpClient) { }

  save(team: Team) {
    
    return this.http.post(this.allTeamUrl, JSON.stringify(team), {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  updateTeam(Team: Team): Observable<Team> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    console.log(JSON.stringify(Team));
    return this.http.post<Team>(this.allTeamUrl + "/update", JSON.stringify(Team), { headers: headers });
  }

  getAllTeam() {
    return this.http.get<Team[]>(this.allTeamUrl);
  }
  
  deleteAdmin(id: number): Observable<Team> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.get<Team>(this.allTeamUrl + '/delete/' + id, { headers: headers });
  }

  
}