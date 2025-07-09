// Simulate test.step
log.info("Test Step: Adding new todo...");

def randomTodo = getRandomProductName();
def randomUID = getRandomName().hashCode().abs() % 1000 + 1;
def randomBool = (getRandomEmail().hashCode() % 2 == 0);

def requestBody = [
    "todo": randomTodo,
    "userId": randomUID,
    "completed": randomBool
];

def headers = [
    "Content-Type": "application/json"
];

makeHttpRequest(POST, "/todos/add", requestBody, headers);

vars.put("randomTodo", randomTodo);
vars.put("randomUID", randomUID.toString());
vars.put("randomBool", randomBool.toString());

log.info("Test Step: Finished adding new todo.");