// Simulate test.step
log.info("Test Step: Updating todo...");

def todoId = getSetting("default_user_id");

def requestBody = [
    "completed": true
];

def headers = [
    "Content-Type": "application/json"
];

makeHttpRequest(PUT, "/todos/${todoId}", requestBody, headers);

vars.put("todoId", todoId);

log.info("Test Step: Finished updating todo with ID: ${todoId}.");