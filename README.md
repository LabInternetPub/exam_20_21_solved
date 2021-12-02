# Internet applications lab
## December, 9th 2020

In this repository you'll find a code that is very familiar to you. It is the classroom example we've been usind during
the course. 

The code already has the following REST API entries working correctly

*	@GetMapping("/students")
*	@GetMapping("/students/{id}")
*	@GetMapping("/students/me")
*	@GetMapping("/classrooms")
*	@GetMapping("/classrooms/allocations") 
*	@GetMapping("/classrooms/{name}/allocations")
*	@GetMapping("/classrooms/allocations/{dayOfWeek}") with variable *full=true* by default
*	@PostMapping("/students")
*	@PostMapping("/classrooms/{name}/allocations/{dayOfWeek}/students/{studentId}")

During the exam you'll have to add the following API entries:
* /classrooms/capacity  --> in order to increase the capacity of a classroom (TODO 3)
* /students/house --> to list all students with their house (TODO 6). You'll see that each student may have a house from the Games of Throne
* /students/{name}/house --> in order to add a random house from an external API (TODO 7)	
	
There are comments beginning with "TODO" that explain what you are supposed to do during the exam. Use the TODO tool of your IDE to quickly find them.
For a TODO to be counted as correct, it must work. If you get stock 
in one of them, just leave it and carry on. If you have time later on, you'll come back to it.
**Please do NOT delete the TODOs once you've finished them**

There are 8 TODOs or exercises to be done during the exam. They are numbered but all are independent and can be done in any order.
* TODO 1: control the case where a new student is created with an already existing name: make sure that the client receives the
appropriate message and http status.
* TODO 2: when allocation a student in a classroom add a new condition to avoid a student to be allocated in the same classroom more than three days a week.
For example, Pepe is already allocated 3 days in classroom 104 and cannot be allocated a fourth day (thursday or friday).
* TODO 3: work with AOP. Log a message each time a method that returns a StudentDTO is called.
* TODO 4: increase the capacity of a classroom (/classrooms/capacity). Make sure that the capacity in not decreased. Save the change in the database
* TODO 5 (2 points): get a list of students with their houses (/students/house). Note that each student may have one house at most but
a house may have more than one student.
* TODO 6 (2 points): update the house of a given student (/students/{name}/house). In this case, the student is identified with his/her name rather than
his/her id. You'll have to get the house from an external REST API
* TODO 7: make the login form work from a different origin (the file system). You'll need to configure the CORS
* TODO 8: work with security in order to make it work as desired

