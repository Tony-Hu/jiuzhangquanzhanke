import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CourseDto } from 'app/shared/model/course-dto.model';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable()
export class CourseService {
    private courseAddressUrl = SERVER_API_URL + '/api/course/findAllCoursesDto';

    private addCourseUrl = SERVER_API_URL + '/api/course/addCourse';

    private withdrawCourseUrl = SERVER_API_URL + '/api/course/withdrawCourse';

    private registeredCourseUrl = SERVER_API_URL + '/api/course/getAllRegisteredCourses';

    constructor(private http: HttpClient) {}

    getCourseInfo(): Observable<CourseDto[]> {
        return this.http.get<CourseDto[]>(`${this.courseAddressUrl}`);
    }

    registerCourse(course: any): Observable<any> {
        return this.http.post(this.addCourseUrl, course);
    }

    withdrawCourse(course: any): Observable<any> {
        const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }), body: course };
        return this.http.delete(this.withdrawCourseUrl, httpOptions);
    }
    getAllRegisteredCourses(): Observable<CourseDto[]> {
        return this.http.get<CourseDto[]>(`${this.registeredCourseUrl}`);
    }
}
