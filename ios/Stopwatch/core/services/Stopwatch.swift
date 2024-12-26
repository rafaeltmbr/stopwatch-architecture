import Foundation

enum StopwatchStatus {
    case INITIAL
    case RUNNING
    case PAUSED
}

struct StopwatchState: Equatable, Hashable {
    var seconds: Float = 0.0
    var status: StopwatchStatus = .INITIAL
}

class Stopwatch<S: StateObserver<StopwatchState>> {
    private(set) var state = ObservableState<S>(StopwatchState())
    
    private var task: Task<(), Never>? = nil

    func start() {
        guard state.state.status == .INITIAL else { return }
        
        loop()
    }
    
    func resume() {
        guard state.state.status == .PAUSED else { return }
        
        loop()
    }
    
    func pause() {
        guard state.state.status == .RUNNING else { return }
        
        task?.cancel()
        task = nil
        state.updateState(StopwatchState(seconds: state.state.seconds, status: .PAUSED))
    }
    
    func reset() {
        guard state.state.status == .PAUSED else { return }
        
        state.updateState(StopwatchState())
    }
    
    private func loop() {
        state.updateState(StopwatchState(seconds: state.state.seconds, status: .RUNNING))
        
        task = Task {@MainActor in
            while state.state.status == .RUNNING {
                state.updateState(StopwatchState(seconds: state.state.seconds + 0.1, status: .RUNNING))
                try? await Task.sleep(for: .milliseconds(100))
            }
        }
    }
}
