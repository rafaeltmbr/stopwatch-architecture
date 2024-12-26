struct ViewData {
    let timeFormatted: String
    let status: StopwatchStatus
}

class StopwatchViewModel<S: StateObserver<ViewData>>: StateObserver {
    private let stopwatch = Stopwatch<StopwatchViewModel>()
    
    private(set) var state = ObservableState<S>(ViewData(timeFormatted: "0.0 s", status: .INITIAL))
    
    init() {
        stopwatch.state.addObserver(self)
    }
    
    func startOrResume() {
        switch stopwatch.state.state.status {
        case .INITIAL:
            stopwatch.start()
        case .PAUSED:
            stopwatch.resume()
        default: break
        }
    }
    
    func pause() {
        stopwatch.pause()
    }
        
    func reset() {
        stopwatch.reset()
    }
    
    func onStateChange(_ state: StopwatchState) {
        self.state.updateState(
            ViewData(
                timeFormatted: String(format: "%.1f s", state.seconds),
                status: state.status
            )
        )
    }
}
