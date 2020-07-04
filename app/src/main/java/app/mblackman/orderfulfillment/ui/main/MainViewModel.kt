package app.mblackman.orderfulfillment.ui.main

import androidx.lifecycle.ViewModel
import app.mblackman.orderfulfillment.data.repository.OrderRepository

/**
 * The main application logic to control user experience.
 */
class MainViewModel(private val orderRepository: OrderRepository) : ViewModel()
