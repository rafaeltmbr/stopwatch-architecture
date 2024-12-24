import Foundation

class StateObserver<T>: Identifiable, Equatable, Hashable {
    let id = UUID()

    func onStateChange(_ state: T) { }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    static func == (lhs: StateObserver<T>, rhs: StateObserver<T>) -> Bool {
        lhs.id == rhs.id
    }
}

class ObservableState<T, S: StateObserver<T>> {
    private(set) var state: T
    
    private var observers: Set<S> = []
    
    init(_ state: T) {
        self.state = state
    }

    func addObserver(_ observer: S) {
        observers.insert(observer)
    }
    
    func removeObserver(_ observer: S) {
        observers.remove(observer)
    }
    
    func updateState(_ state: T) {
        self.state = state

        for observer in observers {
            observer.onStateChange(self.state)
        }
    }
}
