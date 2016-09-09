# between system and system administrator

## log-in

### Basic course

1. An user accesses to micropress. Micropress displays log-in page.
1. An user types his user name and password, and send them. Micropress validates them and displays dashboard.

### Alternative courses

An user accesses to micropress. Micropress is too busy so it displays error page.

An user fills wrong username and password, and send them. Micropress validates them and returns log-in failure.

An user fills correct username and password, but he is expeled.

An user types his user name and password, and send them. Micropress validates them and redirects to a page which he was using.

## invite user

### Basic course

1. An user requests user invitation page. Micropress validates his privilege. Micorpress returns user invitation page.
1. An user fills input form(mail address, privilege), and send it. Micropress validates them, and return complete page.
1. Micropress sends e-mail to invited user.

### Alternative courses

An user request user invitation page. An user does not have system administrator privilege, so micropress returns 403 page.

An user hacks API to invite user. Micropress validates his access-token, and reject his request. He receives 403 response.

An user sends request to Micropress to invite someone, but he is already invited by another system administrator. Micropress rejects invitation request and return 400 status.

Micropress could not send e-mail to specified address, but micropress does nothing.

An user sends invitation to a wrong address. He accesses to a inivitation list page. He delete invitation. Micropress deletes invitation information.

## view invitations

### Basic course

1. An user requests invitations list page, and micropress validates his privilege. Then micropress returns invitations list.

### Alternative courses

An user is not an administrator, so micropress returns 403 error.

## withdraw invitation

### Basic course

1. An user requests invitations list page, and micropress returns it.
1. An user select an invitation to withdraw, so micropress withdraws it.

### Alternative courses

He mis-typed invitation address, and he noticed it just after sending invitation. So he clicked 'cancel' button in invitation completion page.

## accept invitation

### Basic course

1. An user receives email from micropress, and he clicks link to accept invitation.
1. Micropress returns invitation acception page. He select to join the micropress.
1. Micropress returns welcome page. Then the user jumps to his dashboard by 'go to dashboard' button.

## expel user

### Basic course

1. An user request user list page. Micropress validates his privilege and returns user list page.
1. An user selects a target user. Micropress displays alert. He select 'yes', then micropress disables a target user account.

### Alternative courses

An user selects himself to a expulsion target. Micropress returns 400 status.

## change status of any other users

### Basic Course

1. An user request user list page. Micropress validates user privilege, and displays user list page.
1. An user selects another user to edit. Micropress displays his user information edit page.
1. An user edits another user's username, pic, email, password, privilege, status, etc. Then User sends this information to micropress. Micropress validates his request and update user account information. Micropress displays user list page and success message.

### Alternative courses

Micropress fails to update user status, so it returns user information edit page and failure message.

An user changes another user's email address, so micropress sends confirmation mail to updated address.

## change their own account information

### Basic course

1. An user requests profile page. Micropress displays his profile page.
1. An user edits his own profile, then sends it to micropress. Micropress validates it and update profile.
1. If

### Alternative courses

An user sends wrong information. Micropress returns 400 status and profile page.

## configure cooperation with other monitoring tools

### Basic course

1. An user accesses cooperation page. Micropress displays it.
1. An user checks api key, and configure other monitoring tool to check the API to check status. Micropress returns its status(memory, cpu, jdk status, etc) throgh the API.

### Alternative courses

## configure publishement webhook

### Basic course

1. An user accesses cooperation page. Micropress displays it.
1. An user selects webhook page. Micropress displays it.
1. An user configure the webhook to post to facebook when an article is published. Micropress checks webhook when it publishes articles, and kick it.

### Alternative courses
