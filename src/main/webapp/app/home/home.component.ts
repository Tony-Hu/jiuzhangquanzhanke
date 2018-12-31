import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';
import { Course } from './course.model';
import { CourseService } from 'app/shared/service/CourseService';
import { CourseDto } from 'app/shared/model/course-dto.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    registeredCourses: CourseDto[];
    // unRegisteredCourses: CourseDto[];
    showRegisteredCourses: boolean;
    showNotRegisteredCourses: boolean;
    registeredCoursesStr: string;
    notRegisteredCoursesStr: string;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private courseService: CourseService
    ) {}

    courses: CourseDto[] = [
        // new Course('testCourse1', 'USA', 'testContent1', '100'),
        // new Course('testCourse2', 'USA', 'testContent2', '200'),
        // new Course('testCourse3', 'CHN', 'testContent3', '300')
    ];

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
            this.updateAllCoursesData();
        });
        this.registerAuthenticationSuccess();
        this.registeredCourses = [];
        // this.unRegisteredCourses = [];
        this.showRegisteredCourses = false;
        this.showNotRegisteredCourses = false;
        this.registeredCoursesStr = '显示';
        this.notRegisteredCoursesStr = '显示';
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
                // Do a refresh after successful login
                this.updateAllCoursesData();
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    isTeacher(): boolean {
        return this.account.authorities.includes('ROLE_TEACHER');
    }

    isStudent(): boolean {
        return this.account.authorities.includes('ROLE_STUDENT');
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    getAllCourses() {
        this.courseService.getCourseInfo().subscribe(curDto => {
            if (!curDto) {
                this.courses = [];
            } else {
                this.courses = curDto;
            }
        });
    }

    clearAllCourses() {
        this.courses = [];
    }

    // TODO handle error later
    registerCourse(course: any) {
        this.courseService.registerCourse(course).subscribe(
            () => {
                // Do a refresh after successful register
                this.updateAllCoursesData();
                alert('选课成功！');
            },
            response => {
                alert('选课失败！');
            }
        );
    }

    withdrawCourse(course: any) {
        this.courseService.withdrawCourse(course).subscribe(
            () => {
                // Do a refresh after successful withdraw
                this.updateAllCoursesData();
                alert('删课成功！');
            },
            response => {
                alert('删课失败！');
            }
        );
    }

    getAllRegisteredCourses() {
        this.courseService.getAllRegisteredCourses().subscribe(registeredCourses => {
            if (!registeredCourses) {
                this.registeredCourses = [];
            } else {
                this.registeredCourses = registeredCourses;
            }
        });
    }

    toggleRegisteredCourses() {
        this.showRegisteredCourses = !this.showRegisteredCourses;
        if (this.showRegisteredCourses) {
            this.registeredCoursesStr = '隐藏';
        } else {
            this.registeredCoursesStr = '显示';
        }
    }

    toggleNotRegisteredCourses() {
        this.showNotRegisteredCourses = !this.showNotRegisteredCourses;
        if (this.showNotRegisteredCourses) {
            this.notRegisteredCoursesStr = '隐藏';
        } else {
            this.notRegisteredCoursesStr = '显示';
        }
    }

    // filterUnregisteredCourses() {
    //     this.unRegisteredCourses = [];
    //     debugger;
    //     for (const course of this.courses) {
    //         if (!this.registeredCourses.map(c => c.courseName).includes(course.courseName)) {
    //             this.unRegisteredCourses.push(course);
    //         }
    //     }
    // }

    updateAllCoursesData() {
        this.getAllCourses();
        this.getAllRegisteredCourses();
        // this.filterUnregisteredCourses();
    }

    hasRegistered(course: CourseDto) {
        return this.registeredCourses.map(c => c.courseName).includes(course.courseName);
    }
}
