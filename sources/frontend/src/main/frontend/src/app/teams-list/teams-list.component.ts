import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog} from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatPaginator} from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { DialogService } from '../service/dialog-service';
import { TeamService } from '../service/team.service';


@Component({
  selector: 'app-all-teams',
  templateUrl: './teams-list.component.html',
  styleUrls: ['./teams-list.component.css'],
  providers: [TeamService]
})
export class TeamListComponent implements OnInit {
  // propriété
  alladmins: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'responsable', 'entite', 'edit', 'delete'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private teamService: TeamService, private dialog: MatDialog, private dialogService: DialogService) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit(): void {
    //this.loadData();
  }

  onCreate() {
    console.log("debut onCreate()");
   
  }

  onEdit(team) {
    console.log("debut onEdit()");
  }

  onDelete(team) {

    console.log("on delete")
  }

  onDisplay() {

  }

  onSearchClear() {
    this.searchKey = "";
  }
  applyFilter() {
    const search = this.searchKey.trim().toLowerCase();
    this.dataSource.filter = search;
  }

  loadData() {
    
  }

  selectRow(row) {

  }
}


