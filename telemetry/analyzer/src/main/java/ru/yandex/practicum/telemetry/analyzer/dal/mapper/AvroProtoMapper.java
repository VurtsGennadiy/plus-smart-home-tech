package ru.yandex.practicum.telemetry.analyzer.dal.mapper;

import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

public class AvroProtoMapper {
    public static ActionTypeProto toActionTypeProto(ActionTypeAvro actionTypeAvro) {
        return switch (actionTypeAvro) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }
}
