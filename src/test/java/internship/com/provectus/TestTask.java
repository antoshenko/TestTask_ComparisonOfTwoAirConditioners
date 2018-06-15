////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PROVECTUS - TEST TASK
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//- В коде установленны задерки, для полной загрузки страниц
//- В коде реализована возможность открытия списка кондиционеров через строку поиска (закомментирована)

package internship.com.provectus;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class TestTask
{
	public void tasksExecution() throws InterruptedException
	{
		// Установка пути к WebDriver для Google Chrome
		System.setProperty("webdriver.chrome.driver", "C:/Java/Selenium/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// Открытие web приложения по ссылке "https://rozetka.com.ua/"
		driver.get("https://rozetka.com.ua/");
		Thread.sleep(2000);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Выбор города
		// Открытие всплывающего окна для выбора города
		WebElement rzHeaderCitySelectLink = driver.findElement(By.xpath("//div[@id='city-chooser']//a[@href]"));
		rzHeaderCitySelectLink.click();

		// Выбор гиперссылки "Киев"/"Київ" ("Kiev")
		WebElement rzHeaderCityLink_LocalityID_1 = driver.findElement(By.xpath("//a[@href='#'][contains(text(),'Киев')]"));
		rzHeaderCityLink_LocalityID_1.click();
		Thread.sleep(2000);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Первый вариант вывода списка кондиционеров
		// Выбор гиперссылки "Все категории"
		// Первый варинт работы с сылками
/*		driver.findElement(By.linkText("Все категории")).click();
		Thread.sleep(2000);
		driver.findElement(By.linkText("Кондиционеры")).click();
		Thread.sleep(2000);
*/      // Второй варинт работы с сылками
		WebElement rzMenuLink_AllCategoriesGoods = driver.findElement(By.xpath("//*[contains(text(),'Все категории')]"));
		rzMenuLink_AllCategoriesGoods.click();
		Thread.sleep(2000);
		WebElement rzAllCatLink_AirConditioners = driver.findElement(By.xpath("//*[contains(text(),'Кондиционеры')]"));
		rzAllCatLink_AirConditioners.click();
		Thread.sleep(2000);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Второй вариант вывода списка кондиционеров
		// Ввод текста "кондиционеры" в строку поиска
/*		WebElement rzHeaderSearchInputText = driver.findElement(By.xpath("//input[@class='rz-header-search-input-text passive']"));
		rzHeaderSearchInputText.click();
		rzHeaderSearchInputText.clear();
		rzHeaderSearchInputText.sendKeys("кондиционер");

		// Выполнение поиска - клик на кнопку "Найти"
		WebElement rzHeaderSearchButton = driver.findElement(By.xpath("//button[@class='btn-link-i js-rz-search-button']"));
		rzHeaderSearchButton.click();
		Thread.sleep(2000);
*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ПЕРВЫЙ КОНДИЦИОНЕР
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Клик по гиперссылке 1-го кондиционера в списке
		WebElement rzAllAir_ConditionersLink_AirConditioner_01 = driver.findElement(
			By.xpath("//div[@class='g-i-tile-l g-i-tile-catalog-hover-left-side clearfix']/div[1]//div[@class='g-i-tile-i-title clearfix']/a[1]"));
		rzAllAir_ConditionersLink_AirConditioner_01.click();
		Thread.sleep(2000);

		// Клик по меню "Характеристики"
		WebElement rzDetailNavTabs_Characteristics_01 = driver.findElement(By.xpath("//ul[@id='tabs']/li[@name='characteristics']"));
		rzDetailNavTabs_Characteristics_01.click();
		Thread.sleep(2000);

		// Получение количества наименований характеристик для 1-го кондиционера (1-я колонка таблицы в html коде)
		List<WebElement> rzRowsCountInColumn01_AirConditioner_01 = driver.findElements(By.xpath("//table/tbody/tr/td[1]"));

		// Получение количества самих характеристик для 1-го кондиционера (2-я колонка таблицы в html коде)
		List<WebElement> rzRowsCountInColumn02_AirConditioner_01 = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Формирование таблицы с данными для 1-го кондиционера:
		// - в колонке 0 наименование характеристики
		// - с колонки 1 начинаются сами характеристики

		// Создаем двухмерный масив для хранения всех характеристик
		// Число 25 - взято с запасом, т.к. не известно сколько характеристик может быть в одной строке любого кондиционера
		String[][] rzTableCharacteristics_01 = new String[rzRowsCountInColumn01_AirConditioner_01.size()][25];

		// Заполняем первый столбик таблицы с наименованиями характеристик
		for (int i = 0; i < rzRowsCountInColumn01_AirConditioner_01.size(); i++)
		{
			rzTableCharacteristics_01[i][0] = rzRowsCountInColumn01_AirConditioner_01.get(i).getText();
		}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Создание временного строкового массива
		String[] tempRowsCountInColumn02_AirConditioner_01 = new String[rzRowsCountInColumn02_AirConditioner_01.size()];

		// Т.к. для некоторых наименований указано несколько характеристик и все они содержатся в одной строке,
		// что может сделать сравнение неточным, и могут быть утеряны положительные результаты при сравнении
		// Также нет гарантии, что через определенный промежуток времени на первых двух позициях не окажутся другие
		// модели с другими наборами характеристик
		// Поэтому необходимо извлечь все характеристики по отдельности
		// Массив перебора строк с характеристиками и извлечение характеристик
		for (int j = 0; j < rzRowsCountInColumn02_AirConditioner_01.size(); j++)
		{
			// Присвоение элементам строкового масссива значений из списка наименований Web элементов
			tempRowsCountInColumn02_AirConditioner_01[j] = rzRowsCountInColumn02_AirConditioner_01.get(j).getText();

			// Количество найденных и извлеченных характеристик в одной строке (ячейке) для одного наименования
			int countCharacteristicsInRow = 0;

			// Временная переменная - указатель начальной позиции поиска символа перехода на следующую строку "\n"
			int tempCutStringBegin = 0;

			// Массив перебора строк с характеристиками и их извлечение
			for (int k = 0; k < tempRowsCountInColumn02_AirConditioner_01[j].length(); k++)
			{
				// Поиск символа перехода на следующую строку "\n", который является разделителем характеристик
				if (tempRowsCountInColumn02_AirConditioner_01[j].substring(k, k + 1).equals("\n") == true)
				{
					countCharacteristicsInRow++;

					// Временная переменная - указатель конечной позиции поиска символа перехода на следующую строку "\n"
					int tempCutStringEnd = k;

					// Заполняем таблицу с данными самими характеристиками
					rzTableCharacteristics_01[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_01[j].substring(tempCutStringBegin, tempCutStringEnd);

					// Изменяем указатель начала поиска
					tempCutStringBegin = k + 1;
				}
			}
			// Сохранение последней найденной характеристики в строке
			if (countCharacteristicsInRow > 1)
			{
				countCharacteristicsInRow++;
				rzTableCharacteristics_01[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_01[j].substring(tempCutStringBegin, tempRowsCountInColumn02_AirConditioner_01[j].length());
			}
			// Сохранение характеристики, если она одна в строке
			else
			{
				countCharacteristicsInRow++;
				rzTableCharacteristics_01[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_01[j];
			}
		}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("=============================================================================");
		// Вывод параметров первого кондиционера
		for (int s = 0; s < rzRowsCountInColumn01_AirConditioner_01.size(); s++)
		{
			for (int t = 0; t < 25; t++)
			{
				//Вывод элементов массива [for debugging]
				//System.out.print("["+ s + ";" + t + "]");
				//System.out.print(rzTableCharacteristics[s][t] + "");
				//=============================================================================
				//Вывод всех параметров
				if (t == 0)
				{
					System.out.print(s + 1 + ". " + rzTableCharacteristics_01[s][t] + ":|");
				}
				if (t != 0)
				{
					if (rzTableCharacteristics_01[s][t] != null)
					{
						System.out.print("" + rzTableCharacteristics_01[s][t] + "|");
					}
				}
			}
			System.out.println("");
		}

		System.out.println("=============================================================================");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Возврат на два шага назад к списку кондиционеров, для выбора второго кондиционера
		driver.navigate().back();
		Thread.sleep(2000);
		driver.navigate().back();
		Thread.sleep(2000);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ВТОРОЙ КОНДИЦИОНЕР
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Клик по гиперссылке 2-го кондиционера в списке
		WebElement rzAllAir_ConditionersLink_AirConditioner_02 = driver.findElement(By.xpath("//div[@class='g-i-tile-l g-i-tile-catalog-hover-left-side clearfix']/div[2]//div[@class='g-i-tile-i-title clearfix']/a[1]"));
		rzAllAir_ConditionersLink_AirConditioner_02.click();

		// Клик по меню "Характеристики"
		WebElement rzDetailNavTabs_Characteristics_02 = driver.findElement(By.xpath("//ul[@id='tabs']/li[@name='characteristics']"));
		rzDetailNavTabs_Characteristics_02.click();
		Thread.sleep(2000);

		// Получение количества наименований характеристик для 2-го кондиционера (1-я колонка таблицы в html коде)
		List<WebElement> rzRowsCountInColumn01_AirConditioner_02 = driver.findElements(By.xpath("//table/tbody/tr/td[1]"));

		// Получение количества самих характеристик для 2-го кондиционера (2-я колонка таблицы в html коде)
		List<WebElement> rzRowsCountInColumn02_AirConditioner_02 = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Формирование таблицы с данными для 2-го кондиционера:
		// - в колонке 0 наименование характеристики
		// - с колонки 1 начинаются сами характеристики

		// Создаем двухмерный масив для хранения всех характеристик
		// Число 25 - взято с запасом, т.к. не известно сколько характеристик может быть в одной строке любого кондиционера
		String[][] rzTableCharacteristics_02 = new String[rzRowsCountInColumn01_AirConditioner_02.size()][25];

		// Заполняем первый столбик таблицы с наименований характеристик
		for (int i = 0; i < rzRowsCountInColumn01_AirConditioner_02.size(); i++)
		{
			rzTableCharacteristics_02[i][0] = rzRowsCountInColumn01_AirConditioner_02.get(i).getText();
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// Создание временного строкового массива
		String[] tempRowsCountInColumn02_AirConditioner_02 = new String[rzRowsCountInColumn02_AirConditioner_02.size()];

		// Т.к. для некоторых наименований указано несколько характеристик и все они содержатся в одной строке,
		// что может сделать сравнение неточным, и могут быть утеряны положительные результаты при сравнении
		// Также нет гарантии, что через определенный промежуток времени на первых двух позициях не окажутся другие
		// модели с другими наборами характеристик
		// Поэтому необходимо извлечь все характеристики по отдельности
		// Массив перебора строк с характеристиками и извлечение характеристик
		for (int j = 0; j < rzRowsCountInColumn02_AirConditioner_02.size(); j++)
		{
			// Присвоение элементам строкового масссива значений из списка наименований Web элементов
			tempRowsCountInColumn02_AirConditioner_02[j] = rzRowsCountInColumn02_AirConditioner_02.get(j).getText();

			// Количество найденных и извлеченных характеристик в одной строке (ячейке) для одного наименования
			int countCharacteristicsInRow = 0;

			// Временная переменная - указатель начальной позиции поиска символа перехода на следующую строку "\n"
			int tempCutStringBegin = 0;

			// Массив перебора строк с характеристиками и их извлечение
			for (int k = 0; k < tempRowsCountInColumn02_AirConditioner_02[j].length(); k++)
			{
				// Поиск символа перехода на следующую строку "\n", который является разделителем характеристик
				if (tempRowsCountInColumn02_AirConditioner_02[j].substring(k, k + 1).equals("\n") == true)
				{
					countCharacteristicsInRow++;

					// Временная переменная - указатель конечной позиции поиска символа перехода на следующую строку "\n"
					int tempCutStringEnd = k;

					// Заполняем таблицу с данными самими характеристиками
					rzTableCharacteristics_02[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_02[j].substring(tempCutStringBegin, tempCutStringEnd);

					// Изменяем указатель начала поиска
					tempCutStringBegin = k + 1;
				}
			}

			// Сохранение последней найденной характеристики в строке
			if (countCharacteristicsInRow > 1)
			{
				countCharacteristicsInRow++;
				rzTableCharacteristics_02[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_02[j].substring(tempCutStringBegin, tempRowsCountInColumn02_AirConditioner_02[j].length());
			}
			// Сохранение характеристики, если она одна в строке
			else
			{
				countCharacteristicsInRow++;
				rzTableCharacteristics_02[j][countCharacteristicsInRow] = tempRowsCountInColumn02_AirConditioner_02[j];
			}
		}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("=============================================================================");
		// Вывод параметров первого кондиционера
		for (int s = 0; s < rzRowsCountInColumn01_AirConditioner_02.size(); s++)
		{
			for (int t = 0; t < 25; t++)
			{
				//Вывод элементов массива [for debugging]
				//System.out.print("["+ s + ";" + t + "]");
				//System.out.print(rzTableCharacteristics[s][t] + "");
				//=============================================================================
				//Вывод всех параметров
				if (t == 0)
				{
					System.out.print(s + 1 + ". " + rzTableCharacteristics_02[s][t] + ":|");
				}
				if (t != 0)
				{
					if (rzTableCharacteristics_02[s][t] != null)
					{
						System.out.print("" + rzTableCharacteristics_02[s][t] + "|");
					}
				}
			}
			System.out.println("");
		}

		System.out.println("=============================================================================");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// СРАВНЕНИЕ ПАРАМЕТРОВ
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("=============================================================================");
		System.out.println("Следующие параметры являются одинаковыми:");

		for (int i = 0; i < rzRowsCountInColumn01_AirConditioner_01.size(); i++)
		{
			for (int j = 0; j < rzRowsCountInColumn01_AirConditioner_02.size(); j++)
			{
				if (rzTableCharacteristics_01[i][0].equals(rzTableCharacteristics_02[j][0]) == true)
				{
					boolean onlyOnePrint1 = true;
					for (int k = 1; k < 25; k++)
					{
						if (rzTableCharacteristics_01[i][k] != null)
						{
							for (int l = 1; l < 25; l++)
							{
								if (rzTableCharacteristics_02[j][l] != null)
								{
									if (rzTableCharacteristics_01[i][k].equalsIgnoreCase(rzTableCharacteristics_02[j][l]) == true)
									{
										if ( onlyOnePrint1 == true)
										{
											System.out.println("");
											System.out.print("- " + rzTableCharacteristics_01[i][0] + ": |");
											onlyOnePrint1 = false;
										}
										System.out.print(rzTableCharacteristics_01[i][k] + "|");
									}
								}
							}
						}
					}
 				}
			}
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	}

	public static void main(String[] args) throws InterruptedException
	{
		TestTask task = new TestTask();
		task.tasksExecution();
	}
}