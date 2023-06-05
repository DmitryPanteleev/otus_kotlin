package test

import fixture.client.Client
import io.kotest.core.spec.style.FunSpec
import test.action.v1.searchBank

fun FunSpec.testApiV1(client: Client) {
    context("v1") {
        test("Search bank ok") {
            client.searchBank()
        }
    }
}