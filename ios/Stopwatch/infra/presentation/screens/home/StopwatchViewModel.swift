struct ViewData {
    let timeFormatted: String
    let status: StopwatchStatus
}

class StopwatchViewModel: StateObserver<StopwatchState> {
    private let stopwatch = Stopwatch()
    
    private(set) var state = ObservableState(ViewData(timeFormatted: "0.0 s", status: .INITIAL))
    
    override init() {
        super.init()
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
    
    override func onStateChange(_ state: StopwatchState) {
        self.state.updateState(
            ViewData(
                timeFormatted: String(format: "%.1f s", state.seconds),
                status: state.status
            )
        )
    }
}
