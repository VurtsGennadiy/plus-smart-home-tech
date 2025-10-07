package dal.model;

/**
 * Товар в shopping-store может быть в одном из двух состояний: ACTIVE(«активный») и DEACTIVATE(«неактивный»).
 * Товар переходит в неактивное состояние, если администратор удаляет его.
 */
public enum ProductState {
    ACTIVE,
    DEACTIVATE
}
