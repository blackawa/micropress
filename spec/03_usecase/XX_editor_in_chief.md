# between system and editor in chief

## publish articles

### Basic course

1. An user requests draft list page. Micropress validates his privilege, and displays draft list page.
1. An user select a draft. Micropress displays draft detail page.
1. An user decide to publish it immediately, and requests publishment of the article. Micropress change status of draft.

### Alternative courses

An user requests publishment, but the draft has been already deleted. Micropress returns 400 status, draft list page and failure message.

An user chooses multiple drafts in draft list page, and requests to publish all selected drafts.

An user select the article, and set future date as published date. Micorpress waits publishing it until the time has come.

## Withdraw articles

### Basic course

1. An user requests article list page. Micropress validates his privilege, and displays article list page.
1. An user selects one article, then micropress returns article detail page.
1. An user requests to withdraw article. Micropress displays confirm message. An user select 'yes', then micropress channge article status as private.

### Alternative courses
