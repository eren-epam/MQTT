# Connection information

Connect to the broker (e.g., tcp://broker.hivemq.com:1883).
Subscribe: iot/data-epam ( Qos:1 )


# MQTT payload for CRUD operations
1. Create Operation
Message JSON:
{
  "id": 1,
  "name": "Sensor1",
  "value": 23.5,
  "operation": "CREATE"
}

2. Read Operation
Message JSON:
{
  "id": 1,
  "operation": "READ"
}

3. Update Operation
Message JSON:
{
  "id": 1,
  "name": "UpdatedSensor1",
  "value": 35.75,
  "operation": "UPDATE"
}

4. Delete Operation
Message JSON:
{
  "id": 1,
  "operation": "DELETE"
}
