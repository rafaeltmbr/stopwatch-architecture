import SwiftUI

class StopwatchViewDataAdapter: StateObserver, ObservableObject {
    @Published var data: ViewData
    
    init(_ data: ViewData) {
        self.data = data
    }

    func onStateChange(_ state: ViewData) {
        self.data = state
    }
}

struct StopwatchView<VM: StopwatchViewModel<StopwatchViewDataAdapter>>: View {
    private var vm: VM
    @StateObject private var dataAdpter: StopwatchViewDataAdapter

    init(_ vm: VM = StopwatchViewModel<StopwatchViewDataAdapter>()) {
        self.vm = vm
        let adapter = StopwatchViewDataAdapter(vm.state.state)
        _dataAdpter = StateObject(wrappedValue: adapter)
        self.vm.state.addObserver(adapter)
    }

    var body: some View {
        VStack {
            Text(dataAdpter.data.timeFormatted).font(.title)
            Spacer().frame(height: 16)

            switch dataAdpter.data.status {
            case .INITIAL:
                Button("Start") { self.vm.startOrResume() }

            case .RUNNING:
                Button("Pause") { self.vm.pause() }

            case .PAUSED:
                HStack {
                    Button("Resume") { self.vm.startOrResume() }
                    Spacer().frame(width: 16)
                    Button("Reset") { self.vm.reset() }.foregroundColor(.red)
                }
            }
        }
        .onDisappear {
            self.vm.state.removeObserver(_dataAdpter.wrappedValue)
        }
    }
}

#Preview {
    StopwatchView()
}
