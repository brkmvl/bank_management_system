package bank;
import com.google.gson.*;
import java.lang.reflect.Type;



public class Transactiondeserial implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CLASSNAME", transaction.getClass().getSimpleName());

      JsonElement jsonElement = jsonSerializationContext.serialize(transaction);
      jsonObject.add("INSTANCE", jsonElement);

        return  jsonObject;
    }

    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
       JsonObject jsonObject = jsonElement.getAsJsonObject();
       String className = jsonObject.get("CLASSNAME").getAsString();
      JsonElement instance = jsonObject.get("INSTANCE");
        switch (className) {
            case "Payment":
                return jsonDeserializationContext.deserialize(instance, Payment.class);

            case "IncomingTransfer":
                return jsonDeserializationContext.deserialize(instance, IncomingTransfer.class);

            case "OutgoingTransfer":
                return jsonDeserializationContext.deserialize(instance, OutgoingTransfer.class);

            default:
                throw new JsonParseException("Unknown CLASSNAME: " + className);
        }

    }

}
