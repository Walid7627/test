import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { MatSort} from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator} from '@angular/material/paginator';
import { DialogService } from '../service/dialog-service';
import { SegmentService } from '../service/segment.service';


@Component({
  selector: 'app-all-segments',
  templateUrl: './segments-list.component.html',
  styleUrls: ['./segments-list.component.css'],
  providers: [SegmentService]
})
export class SegmentListComponent implements OnInit {
  // propriété
  alladmins: any;
  dataSource = new MatTableDataSource();
  displayedColumns: string[] = ['nom', 'cpv', 'ape', 'edit', 'delete'];
  resultsLength = 0;
  searchKey: string;

  constructor(private router: Router, private segmentService: SegmentService, private dialog: MatDialog, private dialogService: DialogService) { }

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit(): void {
    this.loadData();
  }

  onCreate() {
    console.log("debut onCreate()");
   
  }

  onEdit(segment) {
    console.log("debut onEdit()");
  }

  onDelete(segment) {

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


