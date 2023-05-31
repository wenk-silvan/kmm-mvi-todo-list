import SwiftUI
import shared

@main
struct iOSApp: App {
    
    private let appModule = AppModule()
    
    @StateObject private var state = AppState(store: nil)
    
	var body: some Scene {
        let store = appModule.store
		WindowGroup {
            NavigationView {
                TodoListScreen(store: store, destinationProvider: {
                    TodoItemScreen(store: store)
                })
            }
            .accentColor(.black)
            .textFieldStyle(.roundedBorder)
            .onAppear {
                state.setStore(store: store)
            }
		}
	}
}
