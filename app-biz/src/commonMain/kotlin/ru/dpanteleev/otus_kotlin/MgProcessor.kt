package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.general.operation
import ru.dpanteleev.otus_kotlin.general.prepareResult
import ru.dpanteleev.otus_kotlin.general.stubs
import ru.dpanteleev.otus_kotlin.models.BorrowerCategoryModel
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.WorkMode
import ru.dpanteleev.otus_kotlin.validation.finishAdFilterValidation
import ru.dpanteleev.otus_kotlin.validation.finishMgValidation
import ru.dpanteleev.otus_kotlin.validation.validateDescriptionHasContent
import ru.dpanteleev.otus_kotlin.validation.validateDescriptionNotEmpty
import ru.dpanteleev.otus_kotlin.validation.validateIdNotEmpty
import ru.dpanteleev.otus_kotlin.validation.validateIdProperFormat
import ru.dpanteleev.otus_kotlin.validation.validateLockNotEmpty
import ru.dpanteleev.otus_kotlin.validation.validateLockProperFormat
import ru.dpanteleev.otus_kotlin.validation.validateTitleHasContent
import ru.dpanteleev.otus_kotlin.validation.validateTitleNotEmpty
import ru.dpanteleev.otus_kotlin.workers.initStatus
import ru.dpanteleev.otus_kotlin.workers.stubCreateSuccess
import ru.dpanteleev.otus_kotlin.workers.stubDbError
import ru.dpanteleev.otus_kotlin.workers.stubDeleteSuccess
import ru.dpanteleev.otus_kotlin.workers.stubNoCase
import ru.dpanteleev.otus_kotlin.workers.stubOffersSuccess
import ru.dpanteleev.otus_kotlin.workers.stubReadSuccess
import ru.dpanteleev.otus_kotlin.workers.stubSearchSuccess
import ru.dpanteleev.otus_kotlin.workers.stubUpdateSuccess
import ru.dpanteleev.otus_kotlin.workers.stubValidationBadDescription
import ru.dpanteleev.otus_kotlin.workers.stubValidationBadId
import ru.dpanteleev.otus_kotlin.workers.stubValidationBadTitle
import ru.dpanteleev.otus_kotlin.validation.validation
import ru.otus.otuskotlin.marketplace.core.chain
import ru.otus.otuskotlin.marketplace.core.rootChain
import ru.otus.otuskotlin.marketplace.core.worker

class MgProcessor {
	suspend fun exec(ctx: Context) {
		// TODO: Rewrite temporary stub solution with BIZ
		require(ctx.workMode == WorkMode.STUB) {
			"Currently working only in STUB mode."
		}

		when (ctx.command) {
			Command.SEARCH -> {
				ctx.mortgageResponse.addAll(Stub.prepareSearchList("Bank1>", BorrowerCategoryModel.EMPLOYEE))
			}

			Command.OFFERS -> {
				ctx.mortgageResponse.addAll(Stub.prepareOffersList("Bank2", BorrowerCategoryModel.CONFIRM_INCOME))
			}

			else -> {
				ctx.mortgageResponse = mutableListOf(Stub.get())
			}
		}
	}

	companion object {
		private val BusinessChain = rootChain<Context> {
			initStatus("Инициализация статуса")
			operation("Создание объявления", Command.CREATE) {
				stubs("Обработка стабов") {
					stubCreateSuccess("Имитация успешной обработки")
					stubValidationBadTitle("Имитация ошибки валидации заголовка")
					stubValidationBadDescription("Имитация ошибки валидации описания")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id = MortgageId.NONE }
					worker("Очистка заголовка") { mgValidating.title = mgValidating.title.trim() }
					worker("Очистка описания") { mgValidating.description = mgValidating.description.trim() }
					validateTitleNotEmpty("Проверка, что заголовок не пуст")
					validateTitleHasContent("Проверка символов")
					validateDescriptionNotEmpty("Проверка, что описание не пусто")
					validateDescriptionHasContent("Проверка символов")

					finishMgValidation("Завершение проверок")
				}
				chain {
					title = "Логика сохранения"
				}
				prepareResult("Подготовка ответа")
			}
			operation("Получить объявление", Command.READ) {
				stubs("Обработка стабов") {
					stubReadSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id =  MortgageId(mgValidating.id.toLong())}
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")

					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика чтения"
				}
				prepareResult("Подготовка ответа")
			}
			operation("Изменить объявление", Command.UPDATE) {
				stubs("Обработка стабов") {
					stubUpdateSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubValidationBadTitle("Имитация ошибки валидации заголовка")
					stubValidationBadDescription("Имитация ошибки валидации описания")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toLong()) }
					worker("Очистка lock") { mgValidating.lock = MgLock(mgValidating.lock.asString().trim()) }
					worker("Очистка заголовка") { mgValidating.title = mgValidating.title.trim() }
					worker("Очистка описания") { mgValidating.description = mgValidating.description.trim() }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")
					validateLockNotEmpty("Проверка на непустой lock")
					validateLockProperFormat("Проверка формата lock")
					validateTitleNotEmpty("Проверка на непустой заголовок")
					validateTitleHasContent("Проверка на наличие содержания в заголовке")
					validateDescriptionNotEmpty("Проверка на непустое описание")
					validateDescriptionHasContent("Проверка на наличие содержания в описании")

					finishMgValidation("Успешное завершение процедуры валидации")
					chain {
						title = "Логика сохранения"
					}
					prepareResult("Подготовка ответа")
				}
			}
			operation("Удалить объявление", Command.DELETE) {
				stubs("Обработка стабов") {
					stubDeleteSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") {
						mgValidating = mortgageRequest.deepCopy()
					}
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toLong()) }
					worker("Очистка lock") { mgValidating.lock = MgLock(mgValidating.lock.asString().trim()) }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")
					validateLockNotEmpty("Проверка на непустой lock")
					validateLockProperFormat("Проверка формата lock")
					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика удаления"
				}
				prepareResult("Подготовка ответа")
			}
			operation("Поиск объявлений", Command.SEARCH) {
				stubs("Обработка стабов") {
					stubSearchSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в adFilterValidating") { mgFilterValidating = filterRequest.copy() }

					finishAdFilterValidation("Успешное завершение процедуры валидации")
				}
				prepareResult("Подготовка ответа")
			}
			operation("Поиск подходящих предложений для объявления", Command.OFFERS) {
				stubs("Обработка стабов") {
					stubOffersSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toLong()) }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")

					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика поиска в БД"
				}
				prepareResult("Подготовка ответа")
			}
		}.build()
	}
}
