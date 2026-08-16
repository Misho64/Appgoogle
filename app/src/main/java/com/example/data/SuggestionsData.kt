package com.example.data

data class CareerCategory(
  val id: String,
  val title: String,
  val icon: String,
  val summaries: List<String>,
  val skills: List<String>,
  val verbs: List<String>,
)

object SuggestionsData {
  val categoriesAr = listOf(
    CareerCategory(
      id = "accounting",
      title = "📊 المحاسبة والمالية",
      icon = "📊",
      summaries = listOf(
        "محاسب ماليات متمرس يمتلك خبرة تتجاوز 4 سنوات في إعداد القوائم المالية، إدارة الحسابات العامة، وإجراء التسويات البنكية ومطابقة الحسابات بدقة عالية وفق معايير المحاسبة الدولية.",
        "أخصائي محاسبة وتدقيق مالي متقن لبرامج ERP وتطبيقات Excel المتقدمة، يتميز بكفاءة عالية في إدارة التدفقات النقدية، إعداد التقارير الضريبية، وتقليل الهدر المالي.",
        "محاسب تكاليف وميزانيات ذو مهارات عالية في تحليل المصروفات، إعداد الموازنات التقديرية، والرقابة المالية لدعم اتخاذ القرارات الإدارية السليمة."
      ),
      skills = listOf(
        "إعداد القوائم المالية (Financial Statements)",
        "التحليل والتخطيط المالي (FP&A)",
        "المحاسبة الضريبية وإقرارات VAT",
        "برامج ERP (Odoo / Dynamics AX)",
        "Excel المتقدم (VLOOKUP & Pivot Tables)",
        "المراجعة والتدقيق الداخلي",
        "إدارة التدفقات النقدية (Cash Flow)",
        "حسابات العملاء والموردين (AR/AP)",
        "إعداد الميزانيات والتسويات البنكية"
      ),
      verbs = listOf(
        "أعددت القوائم المالية السنوية والشهرية وفق المعايير الدولية",
        "حققت خفضاً في الأخطاء المحاسبية بنسبة 25% من خلال أتمتة القيود",
        "أدرت التسويات البنكية ومطابقة حسابات الموردين والعملاء",
        "أشرفت على إعداد الإقرارات الضريبية وضريبة القيمة المضافة"
      )
    ),
    CareerCategory(
      id = "admin",
      title = "🏢 إدارة الأعمال والتنفيذ",
      icon = "🏢",
      summaries = listOf(
        "مدير إجرائي وإداري يمتلك خبرة واسعة في تنظيم العمليات اليومية للمؤسسات، التخطيط الاستراتيجي، وإدارة فرق العمل متعددة المهام لتحقيق الأهداف المؤسسية بكفاءة.",
        "أخصائي إدارة أعمال وتطوير مؤسسي متمكن من إدارة المشاريع، تحسين سياق العمليات (Process Optimization)، والتنسيق بين الأقسام التنفيذية لرفع الإنتاجية."
      ),
      skills = listOf(
        "التخطيط الاستراتيجي والتشغيلي",
        "إدارة المشاريع (Agile / PMP)",
        "إدارة العمليات وسلاسل الإمداد",
        "إدارة فرق العمل والقيادة",
        "إعداد التقارير التنفيذية",
        "حل المشكلات واتخاذ القرار"
      ),
      verbs = listOf(
        "قدت فريق عمل تنفيذي مكون من 12 موظفاً لتحقيق الأهداف التشغيلية",
        "حسّنت كفاءة سير العمل الداخلي بنسبة 30% عبر تقليل الدورة المستندية",
        "طوّرت دليل سياسات وإجراءات العمل الإداري للشركة"
      )
    ),
    CareerCategory(
      id = "dev",
      title = "💻 البرمجة وتكنولوجيا المعلومات",
      icon = "💻",
      summaries = listOf(
        "مهندس برمجيات ومطور واجهات متكامل يمتلك خبرة ممتازة في بناء تطبيقات الويب والأنظمة السحابية باستخدام أحدث الأطر البرمجية، مع الالتزام بكتابة كود نظيف وآمن قابل للتوسع.",
        "مطور تطبيقات ومحلل نظم متخصص في تصميم وتطوير الحلول البرمجية الذكية، إدارة قواعد البيانات، وتحسين كفاءة أداء الأنظمة والتكامل البرمجي (API Integration)."
      ),
      skills = listOf(
        "Kotlin / Jetpack Compose / Android",
        "JavaScript / TypeScript / React",
        "HTML5 / CSS3 / Tailwind CSS",
        "Node.js / Express / Backend",
        "Python & Data Structures",
        "SQL & NoSQL Databases (PostgreSQL/Room)",
        "Git & CI/CD Pipelines"
      ),
      verbs = listOf(
        "بنيت وصممت نظام منصة سحابية تخدم أكثر من 10,000 مستخدم نشط",
        "حسّنت سرعة تحميل الصفحة وأداء التطبيق بنسبة 45%",
        "طوّرت واجهات برمجية RESTful APIs آمنة للتكامل مع الخدمات الخارجية"
      )
    ),
    CareerCategory(
      id = "sales",
      title = "📈 التسويق والمبيعات",
      icon = "📈",
      summaries = listOf(
        "أخصائي تسويق رقمي ومبيعات يمتلك سجل نجاح حافل في زيادة المبيعات، إطلاق الحملات الإعلانية المستهدفة، وتوسيع القاعدة الجماهيرية للعلامات التجارية.",
        "مدير مبيعات وتطوير أعمال متمكن من بناء العلاقات الاستراتيجية مع العملاء، تحليل سلوك السوق، ووضع خطط تسعير وتوزيع تحقق أعلى العوائد الماليّة."
      ),
      skills = listOf(
        "التسويق الرقمي (SEO / SEM)",
        "إدارة حملات Meta & Google Ads",
        "إدارة علاقات العملاء (CRM Systems)",
        "التفاوض وإغلاق الصفقات المبيعاتية",
        "كتابة المحتوى الإعلاني (Copywriting)"
      ),
      verbs = listOf(
        "حققت زيادة في المبيعات السنوية بنسبة 35% في المنطقة المستهدفة",
        "أدرت حملات تسويقية رقمية بأسلوب حقق عائداً على الاستثمار (ROI) بـ 4x",
        "جذبت أكثر من 50 عميلاً رئيسياً جديداً من خلال استراتيجيات التواصل المباشر"
      )
    ),
    CareerCategory(
      id = "design",
      title = "🎨 التصميم والإبداع الرقمي",
      icon = "🎨",
      summaries = listOf(
        "مصمم واجهات وتجربة مستخدم (UI/UX) مبتكر، يركز على تحويل الأفكار المعقدة إلى واجهات رقمية جذابة وسهلة الاستخدام تعزز تفاعل المستهلك وتلبي أهداف العمل.",
        "مصمم جرافيك وهويات بصرية متمكن من إخراج المطبوعات والتصاميم الرقمية الاحترافية باستخدام برامج التصميم المتخصصة وبما يخدم الهوية المؤسسية."
      ),
      skills = listOf(
        "UI/UX Design (Figma / Adobe XD)",
        "Adobe Photoshop & Illustrator",
        "تصميم الهويات البصرية والشعارات",
        "البناء المفهومي (Wireframing & Prototyping)",
        "Design Systems & Material Design"
      ),
      verbs = listOf(
        "صممت نظام واجهات مستخدم متكامل لـ 3 تطبيقات هاتف ذكي",
        "ابتكرت الهوية البصرية الكاملة لعلامة تجارية ناشئة في مجال التقنية",
        "أجريت اختبارات الاستخدام (Usability Testing) التي حسّنت معدل التحويل بـ 20%"
      )
    )
  )

  val categoriesEn = listOf(
    CareerCategory(
      id = "accounting",
      title = "📊 Accounting & Finance",
      icon = "📊",
      summaries = listOf(
        "Detail-oriented Financial Accountant with over 4 years of experience preparing financial statements, managing general ledgers, executing bank reconciliations, and ensuring compliance with IFRS standards.",
        "Financial Analyst & Auditor skilled in ERP software and advanced Excel. Proven track record in optimizing cash flow, preparing tax returns, and reducing financial discrepancies."
      ),
      skills = listOf(
        "Financial Statements Preparation",
        "Financial Planning & Analysis (FP&A)",
        "Tax Accounting & VAT Returns",
        "ERP Systems (Odoo / Dynamics AX)",
        "Advanced Excel (VLOOKUP & Pivot Tables)",
        "Internal Auditing & Compliance",
        "Cash Flow & Budgeting"
      ),
      verbs = listOf(
        "Prepared annual and monthly financial statements in compliance with IFRS standards",
        "Reduced accounting errors by 25% through ledger automation",
        "Managed monthly bank reconciliations and supplier account balances"
      )
    ),
    CareerCategory(
      id = "admin",
      title = "🏢 Business Administration & Management",
      icon = "🏢",
      summaries = listOf(
        "Operations & Administrative Manager with broad experience organizing daily organizational workflow, strategic planning, and leading cross-functional teams to achieve business targets.",
        "Business Administration Specialist adept at project management, process optimization, and executive coordination to boost operational efficiency."
      ),
      skills = listOf(
        "Strategic & Operational Planning",
        "Project Management (Agile / PMP)",
        "Process Optimization & BPR",
        "Team Leadership & Coordination",
        "Executive Reporting",
        "Problem Solving & Decision Making"
      ),
      verbs = listOf(
        "Led an executive team of 12 employees to achieve operational KPIs",
        "Improved internal workflow efficiency by 30% by streamlining documentation cycle",
        "Developed the official company administrative policy and procedure manual"
      )
    ),
    CareerCategory(
      id = "dev",
      title = "💻 Software & IT",
      icon = "💻",
      summaries = listOf(
        "Full-Stack Software Engineer with proven expertise building scalable web and mobile applications using modern frameworks, committed to writing clean, maintainable, and secure code.",
        "Application Developer & Systems Analyst specializing in smart software solutions, database management, and optimizing system performance and API integrations."
      ),
      skills = listOf(
        "Kotlin / Jetpack Compose / Android",
        "JavaScript / TypeScript / React",
        "HTML5 / CSS3 / Tailwind CSS",
        "Node.js / Express / Backend",
        "Python & Data Structures",
        "SQL & NoSQL Databases (PostgreSQL / Room)",
        "Git & Version Control"
      ),
      verbs = listOf(
        "Architected and deployed a cloud platform serving over 10,000 active users",
        "Optimized page load speed and application performance by 45%",
        "Developed secure RESTful APIs for third-party service integrations"
      )
    ),
    CareerCategory(
      id = "sales",
      title = "📈 Sales & Marketing",
      icon = "📈",
      summaries = listOf(
        "Digital Marketing & Sales Specialist with a track record of driving revenue growth, launching targeted ad campaigns, and expanding brand market share.",
        "Business Development Manager skilled in building strategic B2B relationships, market trend analysis, and creating pricing models that maximize ROI."
      ),
      skills = listOf(
        "Digital Marketing (SEO / SEM)",
        "Meta & Google Ads Campaign Management",
        "CRM Systems Management",
        "B2B Sales Negotiation & Closing",
        "Ad Copywriting & Content Strategy"
      ),
      verbs = listOf(
        "Achieved a 35% increase in annual sales across designated key accounts",
        "Managed digital marketing campaigns achieving a 4x Return on Ad Spend (ROAS)",
        "Acquired over 50 new B2B key accounts through targeted outreach strategies"
      )
    ),
    CareerCategory(
      id = "design",
      title = "🎨 Design & UI/UX",
      icon = "🎨",
      summaries = listOf(
        "Creative UI/UX Designer dedicated to converting complex requirements into intuitive, aesthetically pleasing web interfaces that enhance user engagement and drive conversion.",
        "Graphic & Brand Designer skilled in print and digital media using modern design tools, focusing on cohesive brand identity building."
      ),
      skills = listOf(
        "UI/UX Design (Figma / Adobe XD)",
        "Adobe Photoshop & Illustrator",
        "Brand Identity & Logo Design",
        "Wireframing & Interactive Prototyping",
        "Design Systems & Component Libraries"
      ),
      verbs = listOf(
        "Designed a scalable user interface system for 3 mobile applications",
        "Created the full brand identity package for an emerging tech startup",
        "Conducted usability testing sessions that increased conversion rate by 20%"
      )
    )
  )

  fun getCategories(lang: String): List<CareerCategory> {
    return if (lang == "en") categoriesEn else categoriesAr
  }
}
