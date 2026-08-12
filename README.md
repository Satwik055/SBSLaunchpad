<img width="1842" height="308" alt="image" src="https://github.com/user-attachments/assets/fe71394a-782d-4f46-9938-d115abc8e033" />

# SBS Launchpad

SBS Launchpad is a mobile app developed to streamline and modernize the college placement and internship process. <br>Developed for Shaheed Bhagat Singh College, Delhi University.
This app acts as intermediary between students and placement department of the college, Placement coordinators from the placemnt department post the jobs and internship opportunities available from the admin app
<br>
<br>
(Note: Recruiter companies can't use the admin app directly to post the jobs) 

**How the apps integrates in college placement process**

<img width="4478" height="1042" alt="image" src="https://github.com/user-attachments/assets/074be516-5601-4945-8a94-aac01dfb518b" />

---

## Tools and Libraries
SBS Launchpad is built using modern Android development practices and a robust backend infrastructure:

* **Jetpack Compose**
* **Dagger Hilt**
* **Supabase Auth**
* **Supabase Storage**
* **Supabase Realtime** 
* **Firebase Crashlytics**
* **Firebase Config**
* **Firebase Analytics**
* **Firebase Cloud Messaging**
* **Coil**
* **Timber** 
---

## App Features

* **Authentication:**  Secure Sign Up and Log In flows using supabase for verified student accounts.
* **Profile Verification:** Students account can be created after getting it verified from admins
* **Job & Internship Applications:** Browse and apply directly to available job roles and internship opportunities.
* **Search for jobs/internships:** Students can search for jobs and internships available in search bar.
* **Read Receipts:** Floating bubble is displayed for the updates/notices which are not yet seen.
* **Realtime Applicants Count:** Students can check in realtime applicants count in a job/internship post
* **Realtime Application Status:** Students can check the status for applications in a banner below posts, (Applied/Not Applied)
* **Realtime Requirements Check:** Student can see their eligibility status, in a banner below posts for various jobs and apply accordingly (Eligible/Not Eligible)
* **Post Expiry:** Students can see a expiry date in posts, also a live timer in post details (if time<24hrs)
* **Profile Edit Requests:** Students profile edit requests need to be approved by admin first.
* **Updates Board:** Admins can send update messages on student applications. 
* **Notice Board:** Stay up to date with official placement announcements and notices in real time.
* **Profile Management:** Easily view, edit, and keep personal and academic details current.
* **Resume Management:** Upload, preview, and update professional resumes directly within the app.

## Developer Centric Features

* **Fatal Crash Reporting:** Fatal exceptions are recorded in crashlytics dashboard and reported via email.
* **Non Fatal Crash Reporting:** Custom Timber-based error reporting pipeline to intercept non-fatal exceptions recorded in crashlytics and automatically gets reported via slack using webhooks.
* **Maintenance Mode:**: A Firebase config of maintenance mode, makes the app unusable across every device via a barrier screen.
* **Analytics:** Critical analytics metrics are captured using firebase analytics.

## Userflows

**Register/Signup Flow**

<img width="8614" height="6792" alt="image" src="https://github.com/user-attachments/assets/04408d71-e80a-40c9-93a3-f9a9fe5202e4" />

**Login Flow**

<img width="5172" height="3448" alt="image" src="https://github.com/user-attachments/assets/d60c0957-4399-4593-9242-80c6822eceed" />

**Job Internship Apply Flow**

<img width="6764" height="3448" alt="image" src="https://github.com/user-attachments/assets/42ca4052-c63f-4137-982c-5bb479a86123" />

**View Updates/Notice Flow**

<img width="6764" height="3448" alt="image" src="https://github.com/user-attachments/assets/b9b133ea-cc1c-44b6-b756-292aa2d82c26" />

**Edit Account/Edit Resume/Logout Flow**

<img width="6764" height="3448" alt="image" src="https://github.com/user-attachments/assets/e8cff418-6ec4-4863-80cd-d7b6a1fa61a5" />

**Search Flow**

<img width="2387" height="2299" alt="image" src="https://github.com/user-attachments/assets/650b4594-3756-47c6-b609-551f118d937e" />


