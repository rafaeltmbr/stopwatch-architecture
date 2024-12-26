import Foundation

protocol StateObserver<T>: Identifiable {
    associatedtype T
    
    func onStateChange(_ state: T)
}

class ObservableState<S: StateObserver> {
    private(set) var state: S.T
    
    private var observers: Dictionary<S.ID, S> = Dictionary()
    
    init(_ state: S.T) {
        self.state = state
    }

    func addObserver(_ observer: S) {
        observers[observer.id] = observer
    }
    
    func removeObserver(_ observer: S) {
        observers[observer.id] = nil
    }
    
    func updateState(_ state: S.T) {
        self.state = state

        for (_, observer) in observers {
            observer.onStateChange(self.state)
        }
    }
}
