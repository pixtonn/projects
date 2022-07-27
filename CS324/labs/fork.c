#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char *argv[]) {
	int pid;
	int p[2];

	FILE* fp;

	// check for pipe error
	if (pipe(p) < 0) {
		exit(1);
	}

	printf("Starting program; process has pid %d\n", getpid());

	FILE *myFile = fopen("fork-output.txt", "w+");

	if ((pid = fork()) < 0) {
		fprintf(stderr, "Could not fork()");
		exit(1);
	}

	/* BEGIN SECTION A */

	printf("Section A;  pid %d\n", getpid());
	fprintf(myFile, "%s", "SECTION A\n");
	// sleep(30);

	/* END SECTION A */
	if (pid == 0) {
		/* BEGIN SECTION B */

		close(p[0]);
		fp = fdopen(p[1], "w");
		fputs("hello from Section B\n", fp);


		printf("Section B\n");
		fprintf(myFile, "%s", "SECTION B\n");
		// sleep(30);
		// sleep(30);
		printf("Section B done sleeping\n");


		char *newenviron[] = { NULL };

		printf("Program \"%s\" has pid %d. Sleeping.\n", argv[0], getpid());
		// sleep(30);

		if (argc <= 1) {
			printf("No program to exec.  Exiting...\n");
			exit(0);
		}
		printf("Running exec of \"%s\"\n", argv[1]);
		
		dup2(fileno(fp), 1);
		execve(argv[1], &argv[1], newenviron);
		printf("End of program \"%s\".\n", argv[0]);

		exit(0);

		/* END SECTION B */
	} else {
		/* BEGIN SECTION C */
	

		close(p[1]);
		fp = fdopen(p[0], "r");
		char str[99];
		fgets(str, 99, fp);
		printf("%s", str);

		printf("Section C\n");
		fprintf(myFile, "%s", "SECTION C\n");
		// sleep(30);
		 wait(0);
		// sleep(30);
		printf("Section C done sleeping\n");

		exit(0);

		/* END SECTION C */
	}
	/* BEGIN SECTION D */

	printf("Section D\n");
	fprintf(myFile, "%s", "SECTION D\n");
	sleep(30);

	/* END SECTION D */
}

