package CourseScheduleApp;

public class Course {
    private final String courseID;
    private final String courseName;
    private final String teacherName;
    private final String location;
    private final int weekHour;

    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个课程对象，其中包括课程的课程号、课程名称、教师姓名、地点与周学时数，不可以对课程的属性进行修改
    // Representation invariant:
    //   要求课程号、课程名称、教师姓名、地点名称不能为空，周学时数必须为偶数
    // Safety from rep exposure:
    //   CourseID、courseName、teacherName、location、weekHour均被private和final修饰

    public Course(String courseID, String courseName, String teacherName, String location, int weekHour) {
        if(courseID.isBlank()) throw new IllegalArgumentException("courseID can't be blank");
        if(courseName.isBlank()) throw new IllegalArgumentException("courseName can't be blank");
        if(teacherName.isBlank()) throw new IllegalArgumentException("teacherName can't be blank");
        if(location.isBlank()) throw new IllegalArgumentException("location can't be blank");
        if(weekHour % 2 == 1) throw new IllegalArgumentException("weekHour must be even");
        this.courseID = courseID;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.location = location;
        this.weekHour = weekHour;
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        assert !courseID.isBlank();
        assert !courseName.isBlank();
        assert !teacherName.isBlank();
        assert !location.isBlank();
        assert  weekHour % 2 == 0;
    }

    /**
     * 获取当前课程的课程号
     * @return String,当前进程的进程号
     */
    public String getCourseID(){
        checkRep();
        return courseID;
    }

    /**
     * 获取当前课程的课程名称
     * @return String,当前课程的课程名称
     */
    public String getCourseName(){
        checkRep();
        return courseName;
    }

    /**
     * 获取当前课程的教师姓名
     * @return String,当前课程的教师姓名
     */
    public String getTeacherName(){
        checkRep();
        return teacherName;
    }

    /**
     * 获取当前课程的上课地点
     * @return String,当前课程的上课地点
     */
    public String getLocation(){
        checkRep();
        return location;
    }

    /**
     * 获取当前课程的周学时数
     * @return String,当前课程的周学时数
     */
    public int getWeekHour(){
        checkRep();
        return weekHour;
    }
}