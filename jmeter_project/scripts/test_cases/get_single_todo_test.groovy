// Simulate test.step
log.info("Test Step: Getting single todo...");

def todoId = getSetting("default_user_id") ?: getRandomName().hashCode().abs() % 100 + 1;
vars.put("todoId", todoId.toString());

makeHttpRequest(GET, "/todos/${todoId}");
log.info("Test Step: Finished getting single todo with ID: ${todoId}.");