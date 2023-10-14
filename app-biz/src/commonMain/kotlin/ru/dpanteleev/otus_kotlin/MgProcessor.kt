package ru.dpanteleev.otus_kotlin

import ru.dpanteleev.otus_kotlin.general.initRepo
import ru.dpanteleev.otus_kotlin.general.operation
import ru.dpanteleev.otus_kotlin.general.prepareResult
import ru.dpanteleev.otus_kotlin.general.stubs
import ru.dpanteleev.otus_kotlin.models.Command
import ru.dpanteleev.otus_kotlin.models.MgLock
import ru.dpanteleev.otus_kotlin.models.MortgageId
import ru.dpanteleev.otus_kotlin.models.State
import ru.dpanteleev.otus_kotlin.repo.repoCreate
import ru.dpanteleev.otus_kotlin.repo.repoDelete
import ru.dpanteleev.otus_kotlin.repo.repoOffers
import ru.dpanteleev.otus_kotlin.repo.repoPrepareCreate
import ru.dpanteleev.otus_kotlin.repo.repoPrepareDelete
import ru.dpanteleev.otus_kotlin.repo.repoPrepareOffers
import ru.dpanteleev.otus_kotlin.repo.repoPrepareUpdate
import ru.dpanteleev.otus_kotlin.repo.repoRead
import ru.dpanteleev.otus_kotlin.repo.repoSearch
import ru.dpanteleev.otus_kotlin.repo.repoUpdate
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
import ru.dpanteleev.otus_kotlin.validation.validation
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
import ru.otus.otuskotlin.marketplace.core.chain
import ru.otus.otuskotlin.marketplace.core.rootChain
import ru.otus.otuskotlin.marketplace.core.worker

class MgProcessor(val settings: CoreSettings = CoreSettings()) {
	suspend fun exec(ctx: Context) = BusinessChain.exec(ctx.apply { this.settings = this@MgProcessor.settings })

	companion object {
		private val BusinessChain = rootChain<Context> {
			initStatus("Инициализация статуса")
			initRepo("Инициализация репозитория")
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
					repoPrepareCreate("Подготовка объекта для сохранения")
					repoCreate("Создание объявления в БД")
				}
				prepareResult("Подготовка ответа")
			}
			operation("Получить условия ипотеки", Command.READ) {
				stubs("Обработка стабов") {
					stubReadSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toString()) }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")

					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика чтения"
					repoRead("Чтение объявления из БД")
					worker {
						title = "Подготовка ответа для Read"
						on { state == State.ACTIVE }
						handle { mgRepoDone = mgRepoRead }
					}
				}
				prepareResult("Подготовка ответа")
			}
			operation("Изменить условия ипотеки", Command.UPDATE) {
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
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toString()) }
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
						repoRead("Чтение объявления из БД")
						repoPrepareUpdate("Подготовка объекта для обновления")
						repoUpdate("Обновление объявления в БД")
					}
					prepareResult("Подготовка ответа")
				}
			}
			operation("Удалить условия ипотеки", Command.DELETE) {
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
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toString()) }
					worker("Очистка lock") { mgValidating.lock = MgLock(mgValidating.lock.asString().trim()) }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")
					validateLockNotEmpty("Проверка на непустой lock")
					validateLockProperFormat("Проверка формата lock")
					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика удаления"
					repoRead("Чтение объявления из БД")
					repoPrepareDelete("Подготовка объекта для удаления")
					repoDelete("Удаление объявления из БД")
				}
				prepareResult("Подготовка ответа")
			}
			operation("Поиск условия ипотеки", Command.SEARCH) {
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
				repoSearch("Поиск объявления в БД по фильтру")
				prepareResult("Подготовка ответа")
			}
			operation("Поиск подходящих предложений для условий ипотеки", Command.OFFERS) {
				stubs("Обработка стабов") {
					stubOffersSuccess("Имитация успешной обработки")
					stubValidationBadId("Имитация ошибки валидации id")
					stubDbError("Имитация ошибки работы с БД")
					stubNoCase("Ошибка: запрошенный стаб недопустим")
				}
				validation {
					worker("Копируем поля в mgValidating") { mgValidating = mortgageRequest.deepCopy() }
					worker("Очистка id") { mgValidating.id = MortgageId(mgValidating.id.toString()) }
					validateIdNotEmpty("Проверка на непустой id")
					validateIdProperFormat("Проверка формата id")

					finishMgValidation("Успешное завершение процедуры валидации")
				}
				chain {
					title = "Логика поиска в БД"
					repoRead("Чтение объявления из БД")
					repoPrepareOffers("Подготовка данных для поиска предложений")
					repoOffers("Поиск предложений для объявления в БД")
				}
				prepareResult("Подготовка ответа")
			}
		}.build()
	}
}
