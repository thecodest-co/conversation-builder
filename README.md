# conversation-builder

This is a serverless app that can be linked to [SlackBot](https://github.com/thecodest-co/slack-bot-mvp).

On a fixed day of the week at a fixed time, this app draws 3 groups of 3 people from Slack. So we have drawn 9 people,
which are unique and do not repeat. A group is a conversation of 3 people. It is not a channel. The bot adds a meme to
each group (webservice with memes with API access) and starts a conversation by sending some messages.

## Infrastructure

Application runs as a spring cloud function and it is deployed on AWS Lambda. The trigger is set by Amazon EventBridge
Rule (more info: [here](https://blog.shikisoft.com/3-ways-to-schedule-aws-lambda-and-step-functions-state-machines/)).

## Settings

Environment variable SLACKBOT_SERVICE_URL should be set to point to above mentioned SlackBot api.

